package syh.toyproject.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import syh.toyproject.Dto.comment.CommentAddDto;
import syh.toyproject.Dto.comment.CommentEditDto;
import syh.toyproject.Dto.comment.CommentEditStatus;
import syh.toyproject.Dto.comment.EditCommentMode;
import syh.toyproject.Dto.post.PostAddDto;
import syh.toyproject.Dto.post.PostEditDto;
import syh.toyproject.Dto.post.PostEditStatus;
import syh.toyproject.Dto.post.PostSearchCond;
import syh.toyproject.argumentResolver.Login;
import syh.toyproject.argumentResolver.LoginName;
import syh.toyproject.domain.comment.Comment;
import syh.toyproject.domain.member.AuthMember;
import syh.toyproject.domain.post.Post;
import syh.toyproject.paging.PageControl;
import syh.toyproject.paging.PageDto;
import syh.toyproject.paging.SortingDto;
import syh.toyproject.service.comment.CommentService;
import syh.toyproject.service.login.LoginService;
import syh.toyproject.service.post.PostService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final PostService postService;
    private final LoginService loginService;
    private final CommentService commentService;

    @GetMapping("/postHome")
    public String postHome(@ModelAttribute(name = "cond") PostSearchCond cond, Model model,
                           @CookieValue(name = "postSearchTrg", defaultValue = "off") String searchTrg,
                           @ModelAttribute(name = "pageDto") PageDto pageDto,
                           @ModelAttribute(name = "sortingDto") SortingDto sortingDto) {

        log.info("pageDto = {}", pageDto);

        pageDto.pageProcess();
//        PageDto pageDto = new PageDto(page, pageView); // 페이지 사이즈를 변경할 수 있도록
        PageControl pageControl = new PageControl(pageDto, postService.totalCount(cond));
        log.info("pageControl = {}", pageControl);

        model.addAttribute("searchTrg", searchTrg);
        model.addAttribute("pageControl", pageControl);
        model.addAttribute("postList", postService.postListToPostDto(postService.findAll(cond, pageDto, sortingDto)));
        return "board/postHome";
    }

    @PostMapping("/postHome/search")
    public String memberSearchModeChange(@CookieValue(name = "postSearchTrg", defaultValue = "off") String searchTrg,
                                         HttpServletResponse response) {
        if (searchTrg.equals("off")) {
            Cookie postSearchTrg = new Cookie("postSearchTrg", "on");
            postSearchTrg.setPath("/");
            response.addCookie(postSearchTrg);
        }
        else if (searchTrg.equals("on")) {
            Cookie postSearchTrg = new Cookie("postSearchTrg", null);
            postSearchTrg.setMaxAge(0);
            postSearchTrg.setPath("/");
            response.addCookie(postSearchTrg);
        }

        return "redirect:/postHome";
    }


    @GetMapping("/post/add")
    public String addPostForm(@ModelAttribute PostAddDto postAddDto, @LoginName String loginMemberName) {
        if (loginMemberName == null) {
            // redirectAttribute 추가
            return "redirect:/postHome";
            // 비로그인 상태에서 글 작성 하기
//            return "board/postWriteForm";
        }

        // 비회원 글쓰기를 아예 막는다면 이 코드도 지우고, 그냥 session 에서 이름 가져오는 걸로 변경
        postAddDto.setUsername(loginMemberName);
        return "board/postWriteForm";
    }

    @PostMapping("/post/add")
    public String addPost(@Validated @ModelAttribute PostAddDto postAddDto, BindingResult bindingResult,
                          @Login Long loginMemberId, @LoginName String loginMemberName) {
        if (bindingResult.hasErrors()) {
            globalErrorReject(bindingResult, "failed", "게시글 작성");
            return "board/postWriteForm";
        }

        if (loginMemberName != null) { // 비회원 글쓰기는 아직까지는 막는 걸로 ㅇㅇ
            postService.addPost(new Post(postAddDto.getCategory(), postAddDto.getPostTitle(), postAddDto.getPostContent(), loginMemberId));
        }

        return "redirect:/postHome";
    }

    @GetMapping("/post/{postId}/edit") // ##### 수정
    public String editPostForm(@PathVariable Long postId, Model model, RedirectAttributes redirectAttributes,
                               @Login Long loginMemberId) {
        if (!(loginService.authAndAdminCheck(loginMemberId, postService.findByMemberId(postId)))) {
            redirectAttributes.addFlashAttribute("postEditStatus", new PostEditStatus(true));
            return "redirect:/post/{postId}";
        }

        Post findPost = postService.findByPostId(postId);
        model.addAttribute("postEditDto", new PostEditDto(findPost.getPostTitle(), findPost.getPostContent()));
        return "board/editPostForm";

    }

    @PostMapping("/post/{postId}/edit")
    public String editPost(@PathVariable Long postId, RedirectAttributes redirectAttributes, @Login Long loginMemberId,
                           @Validated @ModelAttribute PostEditDto postEditDto, BindingResult bindingResult) {
        if (!(loginService.authAndAdminCheck(loginMemberId, postService.findByMemberId(postId)))) {
            redirectAttributes.addFlashAttribute("postEditStatus", new PostEditStatus(true));
            return "redirect:/post/{postId}";
        }

        if (bindingResult.hasErrors()) {
            return "board/editPostForm";
        }

        postService.editPost(postId, new Post(postEditDto.getCategory(), postEditDto.getPostTitle(), postEditDto.getPostContent()));
        return "redirect:/post/{postId}";
    }

    @PostMapping("/post/{postId}/recommend")
    public String recommendPost(@PathVariable Long postId, @Login Long loginMemberId,  @LoginName String loginMemberName) {
        if (loginMemberName == null) { // 비회원 추천 시 돌려보내기
            // redirect attribute 추가
            return "redirect:/post/{postId}";
        }
        
        // 중복 추천 방지 추가
        if (postService.recommendDuplicateCheck(postId, loginMemberId)) {
            // 추천 중복 불가
            return "redirect:/post/{postId}";
        }

        postService.recommendPost(postId, loginMemberId);
        return "redirect:/post/{postId}";
    }

    @PostMapping("/post/{postId}/delete")
    public String deletePost(@PathVariable Long postId, @Login Long loginMemberId, RedirectAttributes redirectAttributes) {
        if (!(loginService.authAndAdminCheck(loginMemberId, postService.findByMemberId(postId)))) {
            redirectAttributes.addFlashAttribute("postEditStatus", new PostEditStatus(true));
            return "redirect:/post/{postId}";
        }

        postService.deletePost(postId);
        return "redirect:/postHome";
    }

    @GetMapping("/post/{postId}")
    public String postDetail(@PathVariable Long postId, Model model, // postAuth => 게시글 수정 노출 트리거
                             @ModelAttribute(name = "commentAuth") AuthMember commentAuthMember, @ModelAttribute(name = "postAuth") AuthMember postAuthMember,
                             @ModelAttribute CommentEditDto commentEditDto, BindingResult commentEditBindingResult, @ModelAttribute CommentEditStatus commentEditStatus,
                             @ModelAttribute CommentAddDto commentAddDto, BindingResult commentAddBindingResult,
                             @ModelAttribute PostEditStatus postEditStatus, BindingResult postEditBindingResult,
                             @Login Long loginMemberId, @LoginName String loginMemberName,
                             @RequestParam(defaultValue = "1", name = "page") int page,
                             @ModelAttribute(name = "sortingDto") SortingDto sortingDto) {
//        addCommentErrorCheck
        if (model.getAttribute("addCommentError") != null) {
            BindingResult addDtoError = (BindingResult) model.getAttribute("addCommentError");
            commentAddBindingResult.addAllErrors(addDtoError);

            // 비회원 댓글 작성 기능용으로 // 댓글 작성 실패한 bindingResult 값을 받아오는 용도
            CommentAddDto addDto = (CommentAddDto) model.getAttribute("addCommentDto");
            commentAddDto.setCommentWriter(addDto.getCommentWriter());
            commentAddDto.setCommentContent(addDto.getCommentContent());
        }

//        editPostAccessDeniedCheck
        if (postEditStatus.isAccessDenied()) {
            globalErrorReject(postEditBindingResult, "accessDenied.editPost", "게시글수정");
        }

        // loginSessionCheck
        if (loginMemberName != null) {
            commentAddDto.setCommentWriter(loginMemberName); // 로그인 되어있는 경우 작성자 이름 넣어줌.
            commentAuthMember.setAuthMemberId(loginMemberId); // 댓글 수정 권한 있는 회원만 수정 버튼 표시 체크용

            if (loginService.authAndAdminCheck(loginMemberId, postService.findByMemberId(postId))) {
                postAuthMember.setAuthMemberId(loginMemberId); // 게시글 수정 권한 있는 회원만 수정 버튼 표시
            }
        }
        // editPostAuthCheck
        editCommentModeCheck(model, commentEditDto, commentEditBindingResult, commentEditStatus);

        postService.addViewCount(postId);

        PageDto pageDto = new PageDto(page, 5);
        PageControl pageControl = new PageControl(pageDto, commentService.totalCount(postId));

        model.addAttribute("pageControl", pageControl);
        model.addAttribute("post", postService.postToPostDto(postService.findByPostId(postId)));
        model.addAttribute("commentList", commentService.commentListToCommentDto(commentService.findByPostIdAll(postId, pageDto, sortingDto)));
        return "board/postDetail";
    }

    @PostMapping("/post/{postId}/{commentId}")
    public String editCommentModeChange(@PathVariable Long postId, @PathVariable Long commentId, RedirectAttributes redirectAttributes,
                                        @Login Long loginMemberId) {
        editCommentModeChangeAuthAndAdminCheck(commentId, redirectAttributes, loginMemberId);
        return "redirect:/post/{postId}";
    }

    @PostMapping("/post/{postId}/{commentId}/edit") // 포스트 ID와 멤버 ID, 코멘트 ID 가 일치하는 경우에 수정을 누르면 th:if로 입력폼으로 변경 & 저장
    public String editComment(@PathVariable Long postId, @PathVariable Long commentId, RedirectAttributes redirectAttributes,
                              @Validated @ModelAttribute CommentEditDto commentEditDto, BindingResult bindingResult,
                              @Login Long loginMemberId) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentEditStatus", new CommentEditStatus(commentId, EditCommentMode.ERR));
            redirectAttributes.addFlashAttribute("commentEditDtoError", bindingResult);
            return "redirect:/post/{postId}";
        }

        editCommentAuthAndAdminCheck(commentId, redirectAttributes, loginMemberId);

        commentService.editComment(commentId, new Comment(commentEditDto.getCommentContent()));
        return "redirect:/post/{postId}";
    }

    @PostMapping("/post/{postId}")
    public String addComment(@Validated @ModelAttribute CommentAddDto commentAddDto, BindingResult bindingResult,
                             @PathVariable Long postId, RedirectAttributes redirectAttributes,
                             @Login Long loginMemberId, @LoginName String loginMemberName) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addCommentError", bindingResult);
            redirectAttributes.addFlashAttribute("addCommentDto", commentAddDto);
            return "redirect:/post/{postId}";
        }

        addCommentAuthAndAdminCheck(bindingResult, redirectAttributes, loginMemberName);

        commentService.addComment(new Comment(postId, loginMemberId, commentAddDto.getCommentContent()));
        return "redirect:/post/{postId}";
    }

    @PostMapping("/post/{postId}/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId, RedirectAttributes redirectAttributes,
                                @Login Long loginMemberId) {
        editCommentAuthAndAdminCheck(commentId, redirectAttributes, loginMemberId);

        commentService.deleteComment(commentId);
        return "redirect:/post/{postId}";
    }


    private void globalErrorReject(BindingResult bindingResult, String errorCode, Object... errorArgs) {
        bindingResult.reject(errorCode, errorArgs, null);
    }

    private void editCommentModeChangeAuthAndAdminCheck(Long commentId, RedirectAttributes redirectAttributes, Long loginMemberId) {
        if (loginService.authAndAdminCheck(loginMemberId, commentService.findByMemberId(commentId))) {
            redirectAttributes.addFlashAttribute("commentEditStatus", new CommentEditStatus(commentId, EditCommentMode.ON));
        } else { // 세션 X or 권한 X
            redirectAttributes.addFlashAttribute("commentEditStatus", new CommentEditStatus(commentId, true));
        }
    }

    private void editCommentModeCheck(Model model, CommentEditDto commentEditDto, BindingResult commentEditBindingResult, CommentEditStatus commentEditStatus) {
        if (commentEditStatus.isAccessDenied()) { // 권한 없이 댓글 수정 관련 기능 접근 시 에러 처리 (postman 등 접근 방지)
            globalErrorReject(commentEditBindingResult, "accessDenied.editComment", "댓글수정");
        }
        else if (commentEditStatus.getEditCommentMode() == EditCommentMode.ON) {
            commentEditDto.setCommentContent(commentService.findByCommentId(commentEditStatus.getCommentId()).getCommentContent());
        }
        else if (commentEditStatus.getEditCommentMode() == EditCommentMode.ERR) {
            BindingResult editDtoError = (BindingResult) model.getAttribute("commentEditDtoError");
            commentEditBindingResult.addAllErrors(editDtoError);
        }
    }

    private void editCommentAuthAndAdminCheck(Long commentId, RedirectAttributes redirectAttributes, Long loginMemberId) {
        if (!loginService.authAndAdminCheck(loginMemberId, commentService.findByMemberId(commentId))) {
            redirectAttributes.addFlashAttribute("commentEditStatus", new CommentEditStatus(commentId, true));
            // 세션 X or 권한 X
        }
    }

    private void addCommentAuthAndAdminCheck(BindingResult bindingResult, RedirectAttributes redirectAttributes, String loginMemberName) {
        if (loginMemberName == null) { // 일단 회원만 작성 가능 하도록.
            globalErrorReject(bindingResult, "accessDenied.addComment", "댓글작성");
            redirectAttributes.addFlashAttribute("addCommentError", bindingResult);
        }
    }


//    @PostConstruct
//    public void init() {
//        postService.addPost(new Post(1L, "테스트글 제목1", "테스트글 내용1"));
//        postService.addPost(new Post(2L, "테스트글 제목2", "테스트글 내용2"));
//
//        commentService.addComment(new Comment(1L, 1L, "댓글^^"));
//        commentService.addComment(new Comment(2L, 2L, "댓글 달아드렸읍니다2 ^^"));
//    }
}

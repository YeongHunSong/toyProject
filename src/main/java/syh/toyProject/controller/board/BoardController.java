package syh.toyProject.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import syh.toyProject.Dto.comment.CommentAddDto;
import syh.toyProject.Dto.comment.CommentEditDto;
import syh.toyProject.Dto.comment.CommentEditStatus;
import syh.toyProject.Dto.comment.EditCommentMode;
import syh.toyProject.Dto.image.ImageListBoardDto;
import syh.toyProject.Dto.post.PostAddDto;
import syh.toyProject.Dto.post.PostEditDto;
import syh.toyProject.Dto.post.PostEditStatus;
import syh.toyProject.Dto.post.PostSearchCond;
import syh.toyProject.argumentResolver.Login;
import syh.toyProject.argumentResolver.LoginName;
import syh.toyProject.domain.comment.Comment;
import syh.toyProject.domain.image.Image;
import syh.toyProject.domain.image.UploadImage;
import syh.toyProject.domain.member.AuthMember;
import syh.toyProject.domain.post.Post;
import syh.toyProject.paging.PageControl;
import syh.toyProject.paging.PageDto;
import syh.toyProject.paging.SortingDto;
import syh.toyProject.service.comment.CommentService;
import syh.toyProject.service.image.ImageService;
import syh.toyProject.service.image.ImageStore;
import syh.toyProject.service.login.LoginService;
import syh.toyProject.service.post.PostService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final ImageStore imageStore;
    private final PostService postService;
    private final ImageService imageService;
    private final LoginService loginService;
    private final CommentService commentService;

    @Value("${image.dir}")
    private String imageDir;


    @GetMapping("/imageUpload")
    public String fileTest(@ModelAttribute PostAddDto postAddDto) {
        return "image/imageForm";
    }

//    @PostMapping("/imageUpload")
//    public String saveFile(@ModelAttribute PostAddDto postAddDto,
//                           RedirectAttributes redirectAttributes) throws IOException {
//        if (postAddDto.getUploadDate() != null) {
//            log.info("imageUploadDto = {}", postAddDto);
//            List<UploadImage> uploadImages = imageStore.storeFiles(postAddDto, postAddDto.getPostId());
//
//            for (UploadImage uploadImage : uploadImages) {
//                imageService.uploadImage(new Image(uploadImage));
//            }
//            redirectAttributes.addAttribute("postId", postAddDto.getPostId());
//            return "redirect:/imageView/{postId}";
//        }
//        return "image/imageForm";
//    }

//    @GetMapping("/imageView/{postId}")
//    public String imageView(@PathVariable Long postId, Model model) {
//        ImageListBoardDto imageListBoardDto = new ImageListBoardDto(postId, imageService.findByPostId(postId));
//
//        model.addAttribute("imageList", imageListBoardDto);
//        return "image/imageView";
//    }

    @ResponseBody
    @GetMapping("/post/{postId}/image/{serverName}")
    public ResponseEntity<Resource> imageRender(@PathVariable Long postId, @PathVariable String serverName) throws IOException {


//        Resource resource = new InputStreamResource(Files.newInputStream(Paths.get(imageStore.getFullPath(postId, serverName))));
        UrlResource resource = new UrlResource("file:" + imageStore.getFullPath(postId, serverName));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, imageService.getMediaType(serverName))
                .body(resource);
    }


    @GetMapping("/postHome")
    public String postHome(@ModelAttribute(name = "cond") PostSearchCond cond, Model model,
                           @CookieValue(name = "postSearchTrg", defaultValue = "off") String searchTrg,
                           @ModelAttribute(name = "sortingDto") SortingDto sortingDto,
                           @ModelAttribute(name = "pageDto") PageDto pageDto) {
        PageControl pageControl = new PageControl(pageDto, postService.totalCount(cond));

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
            // TODO redirectAttribute 추가
            return "redirect:/postHome";
        }
        return "board/postWriteForm";
    }

    @PostMapping("/post/add")
    public String addPost(@Validated @ModelAttribute PostAddDto postAddDto, BindingResult bindingResult,
                          @Login Long loginMemberId, @LoginName String loginMemberName) throws IOException {
        if (bindingResult.hasErrors()) {
            globalErrorReject(bindingResult, "failed", "게시글 작성");
            return "board/postWriteForm";
        }


        if (loginMemberName != null) { // 로그인 체크
            Long genPostId = postService.addPost(postAddDto.newPost(loginMemberId));
            if (postAddDto.getUploadImages() != null) {
                for (UploadImage uploadImage : imageStore.storeImages(postAddDto, genPostId)) {
                    imageService.uploadImage(new Image(uploadImage));
                }
            }
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
            //TODO redirect attribute 추가
            return "redirect:/post/{postId}";
        }
        
        // 중복 추천 방지 추가
        if (postService.recommendDuplicateCheck(postId, loginMemberId)) {
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

        imageService.deleteImageAll(postId);
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
                             @ModelAttribute(name = "sortingDto") SortingDto sortingDto, @ModelAttribute(name = "pageDto") PageDto pageDto,
                             @RequestParam(name = "pageView", defaultValue = "5") int pageView) {
//        addCommentErrorCheck
        if (model.getAttribute("addCommentError") != null) {
            BindingResult addDtoError = (BindingResult) model.getAttribute("addCommentError");
            commentAddBindingResult.addAllErrors(addDtoError);

            // TODO 비회원 댓글 X 삭제 // 비회원 댓글 작성 기능용으로 // 댓글 작성 실패한 bindingResult 값을 받아오는 용도
            CommentAddDto addDto = (CommentAddDto) model.getAttribute("addCommentDto");
            commentAddDto.setCommentWriter(addDto.getCommentWriter());
            commentAddDto.setCommentContent(addDto.getCommentContent());
        }

        editPostAccessDeniedCheck(postEditStatus, postEditBindingResult);

        // loginSessionCheck
        if (loginMemberName != null) {
            commentAddDto.setCommentWriter(loginMemberName);  // 로그인 되어있는 경우 작성자 이름 넣어줌.
            commentAuthMember.setAuthMemberId(loginMemberId); // 댓글 수정 권한 있는 회원만 수정 버튼 표시 체크용

            if (loginService.authAndAdminCheck(loginMemberId, postService.findByMemberId(postId))) {
                postAuthMember.setAuthMemberId(loginMemberId); // 게시글 수정 권한 있는 회원만 수정 버튼 표시
            }
        }
        // editPostAuthCheck
        editCommentModeCheck(model, commentEditDto, commentEditBindingResult, commentEditStatus);

        postService.addViewCount(postId);
        pageDto.setPageView(pageView);

        model.addAttribute("post", postService.postToPostDto(postService.findByPostId(postId)));
        model.addAttribute("pageControl", new PageControl(pageDto, commentService.totalCount(postId)));
        model.addAttribute("imageList", new ImageListBoardDto(postId, imageService.findByPostId(postId)));
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
            redirectAttributes.addFlashAttribute("addCommentDto", commentAddDto); // TODO
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

    private void editPostAccessDeniedCheck(PostEditStatus postEditStatus, BindingResult postEditBindingResult) {
        if (postEditStatus.isAccessDenied()) {
            globalErrorReject(postEditBindingResult, "accessDenied.editPost", "게시글수정");
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

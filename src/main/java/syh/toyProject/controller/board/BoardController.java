package syh.toyProject.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import syh.toyProject.Dto.image.UploadImage;
import syh.toyProject.Dto.post.PostAddDto;
import syh.toyProject.Dto.post.PostEditDto;
import syh.toyProject.Dto.post.PostEditStatus;
import syh.toyProject.Dto.post.PostSearchCond;
import syh.toyProject.argumentResolver.Login;
import syh.toyProject.argumentResolver.LoginName;
import syh.toyProject.domain.image.Image;
import syh.toyProject.domain.member.AuthMember;
import syh.toyProject.paging.CommentPageDto;
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



    @ResponseBody
    @GetMapping("/post/{postId}/image/{serverName}")
    public ResponseEntity<Resource> renderImage(@PathVariable Long postId, @PathVariable String serverName) throws IOException {
//        Resource resource = new InputStreamResource(Files.newInputStream(Paths.get(imageStore.getFullPath(postId, serverName))));
        UrlResource resource = new UrlResource("file:" + imageStore.getFullPath(postId, serverName));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, imageService.getMediaType(serverName)) // UncheckedException - InvalidMimeTypeException
                .body(resource);
    }



    @PostMapping("/post/{postId}/image/{imageId}/delete")
    public String deleteImage(@PathVariable Long postId, @PathVariable Long imageId, @Login Long loginMemberId) {
        if (!(loginService.authAndAdminCheck(loginMemberId, postService.findByMemberId(postId)))) {
//            redirectAttributes.addFlashAttribute("postEditStatus", PostEditStatus.accessDenied());
            return "redirect:/post/{postId}";
        }

        imageService.deleteImage(Image.deleteDto(postId, imageId));
        return "redirect:/post/{postId}/edit";
    }



    @GetMapping("/postHome")
    public String postHome(@ModelAttribute(name = "cond") PostSearchCond cond, Model model,
                           @CookieValue(name = "postSearchTrg", defaultValue = "off") String searchTrg,
                           @ModelAttribute(name = "sortingDto") SortingDto sortingDto,
                           @ModelAttribute(name = "pageDto") PageDto pageDto) {

        model.addAttribute("searchTrg", searchTrg);
        model.addAttribute("pageControl",
                PageControl.create(pageDto, postService.totalCount(cond)));
        model.addAttribute("postList",
                postService.postListToPostDto(postService.findAll(cond, pageDto, sortingDto)));
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
        return "board/addPostForm";
    }



    @PostMapping("/post/add")
    public String addPost(@Validated @ModelAttribute PostAddDto postAddDto, BindingResult bindingResult,
                          @Login Long loginMemberId, @LoginName String loginMemberName) throws IOException {
        if (bindingResult.hasErrors()) {
            globalErrorReject(bindingResult, "failed", "게시글 작성");
            return "board/addPostForm";
        }

        if (loginMemberName != null) { // 로그인 체크
            Long genPostId = postService.addPost(postAddDto.newPost(loginMemberId));
            if (postAddDto.getUploadImages().get(0).getSize() != 0L) { // 이미지 파일이 있을 경우 업로드
                for (UploadImage uploadImage : imageStore.storeImages(postAddDto.getUploadImages(), genPostId)) {
                    imageService.uploadImage(uploadImage);
                }
            }
        }

        return "redirect:/postHome";
    }



    @GetMapping("/post/{postId}/edit") // ##### 수정
    public String editPostForm(@PathVariable Long postId, Model model, RedirectAttributes redirectAttributes,
                               @Login Long loginMemberId) {
        if (!(loginService.authAndAdminCheck(loginMemberId, postService.findByMemberId(postId)))) {
            redirectAttributes.addFlashAttribute("postEditStatus", PostEditStatus.accessDenied());
            return "redirect:/post/{postId}";
        }

        model.addAttribute("postEditDto", PostEditDto.create(postService.findByPostId(postId), imageService.findByPostId(postId)));
        return "board/editPostForm";
    }



    @PostMapping("/post/{postId}/edit")
    public String editPost(@PathVariable Long postId, RedirectAttributes redirectAttributes, @Login Long loginMemberId,
                           @Validated @ModelAttribute PostEditDto postEditDto, BindingResult bindingResult) throws IOException {
        if (!(loginService.authAndAdminCheck(loginMemberId, postService.findByMemberId(postId)))) {
            redirectAttributes.addFlashAttribute("postEditStatus", PostEditStatus.accessDenied());
            return "redirect:/post/{postId}";
        }

        if (bindingResult.hasErrors()) {
            return "board/editPostForm";
        }

        postService.editPost(postId, postEditDto);
        if (postEditDto.getUploadImages().get(0).getSize() != 0L) {
            for (UploadImage uploadImage : imageStore.storeImages(postEditDto.getUploadImages(), postId)) {
                imageService.uploadImage(uploadImage);
            }
        }

        return "redirect:/post/{postId}";
    }



    @PostMapping("/post/{postId}/recommend")
    public String recommendPost(@PathVariable Long postId, @Login Long loginMemberId, @LoginName String loginMemberName,
                                RedirectAttributes redirectAttributes) {
        if (loginMemberName == null) {
            recommendErrorMessage(redirectAttributes, "로그인이 필요합니다.");
            return "redirect:/post/{postId}";
        }
        
        if (postService.recommendDuplicateCheck(postId, loginMemberId)) {
            recommendErrorMessage(redirectAttributes, "추천은 한 번만 가능합니다.");
            return "redirect:/post/{postId}";
        }

        postService.recommendPost(postId, loginMemberId);
        return "redirect:/post/{postId}";
    }



    @PostMapping("/post/{postId}/delete")
    public String deletePost(@PathVariable Long postId, @Login Long loginMemberId, RedirectAttributes redirectAttributes) {
        if (!(loginService.authAndAdminCheck(loginMemberId, postService.findByMemberId(postId)))) {
            redirectAttributes.addFlashAttribute("postEditStatus", PostEditStatus.accessDenied());
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
                             @ModelAttribute(name = "sortingDto") SortingDto sortingDto, @ModelAttribute(name = "pageDto") CommentPageDto pageDto) {
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

        model.addAttribute("postDto", postService.postToPostDto(postService.findByPostId(postId)));
        model.addAttribute("imageList", ImageListBoardDto.create(postId, imageService.findByPostId(postId))); // TODO postDto 와 통합
        model.addAttribute("pageControl", PageControl.create(pageDto, commentService.totalCount(postId)));
        model.addAttribute("commentList", commentService.commentListToCommentDto(commentService.findByPostIdAll(postId, pageDto, sortingDto)));
        return "board/postDetail";
    }



    @PostMapping("/post/{postId}/{commentId}")
    public String editCommentModeChange(@PathVariable Long postId, @PathVariable Long commentId, RedirectAttributes redirectAttributes,
                                        @Login Long loginMemberId, @ModelAttribute SortingDto sortingDto, @ModelAttribute CommentPageDto pageDto) {
        editCommentModeChangeAuthAndAdminCheck(commentId, redirectAttributes, loginMemberId);
        redirectAttributes.addFlashAttribute("sortingDto", sortingDto);
        redirectAttributes.addFlashAttribute("pageDto", pageDto);
        return "redirect:/post/{postId}";
    }

    @PostMapping("/post/{postId}/{commentId}/edit") // 포스트 ID와 멤버 ID, 코멘트 ID 가 일치하는 경우에 수정을 누르면 th:if로 입력폼으로 변경 & 저장
    public String editComment(@PathVariable Long postId, @PathVariable Long commentId, RedirectAttributes redirectAttributes,
                              @Validated @ModelAttribute CommentEditDto commentEditDto, BindingResult bindingResult, @Login Long loginMemberId,
                              @ModelAttribute SortingDto sortingDto, @ModelAttribute CommentPageDto pageDto) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentEditStatus", CommentEditStatus.editError(commentId));
            redirectAttributes.addFlashAttribute("commentEditDtoError", bindingResult);
            return "redirect:/post/{postId}";
        }

        editCommentAuthAndAdminCheck(commentId, redirectAttributes, loginMemberId);
        redirectAttributes.addFlashAttribute("sortingDto", sortingDto);
        redirectAttributes.addFlashAttribute("pageDto", pageDto);
        commentService.editComment(commentId, commentEditDto);
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
        commentService.addComment(postId, loginMemberId, commentAddDto);
        return "redirect:/post/{postId}";
    }



    @PostMapping("/post/{postId}/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId, RedirectAttributes redirectAttributes,
                                @Login Long loginMemberId, @ModelAttribute SortingDto sortingDto, @ModelAttribute CommentPageDto pageDto) {
        editCommentAuthAndAdminCheck(commentId, redirectAttributes, loginMemberId);
        redirectAttributes.addFlashAttribute("sortingDto", sortingDto);
        redirectAttributes.addFlashAttribute("pageDto", pageDto);
        commentService.deleteComment(commentId);
        return "redirect:/post/{postId}";
    }



    private void globalErrorReject(BindingResult bindingResult, String errorCode, Object... errorArgs) {
        bindingResult.reject(errorCode, errorArgs, null);
    }

    private void editCommentModeChangeAuthAndAdminCheck(Long commentId, RedirectAttributes redirectAttributes, Long loginMemberId) {
        if (loginService.authAndAdminCheck(loginMemberId, commentService.findByMemberId(commentId))) {
            redirectAttributes.addFlashAttribute("commentEditStatus", CommentEditStatus.editOn(commentId));
        } else { // 세션 X or 권한 X
            redirectAttributes.addFlashAttribute("commentEditStatus", CommentEditStatus.accessDenied(commentId));
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
            redirectAttributes.addFlashAttribute("commentEditStatus", CommentEditStatus.accessDenied(commentId));
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

    private void recommendErrorMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("recErrMessage", message);
    }
}



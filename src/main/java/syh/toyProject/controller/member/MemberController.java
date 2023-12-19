package syh.toyProject.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import syh.toyProject.Dto.login.LoginStatus;
import syh.toyProject.Dto.member.MemberEditDto;
import syh.toyProject.Dto.member.MemberSignupDto;
import syh.toyProject.argumentResolver.Login;
import syh.toyProject.domain.member.Member;
import syh.toyProject.paging.PageControl;
import syh.toyProject.paging.PageDto;
import syh.toyProject.paging.SortingDto;
import syh.toyProject.service.comment.CommentService;
import syh.toyProject.service.login.LoginService;
import syh.toyProject.service.member.MemberService;
import syh.toyProject.service.post.PostService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    /**
     * // @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) == @Login
     * // @SessionAttribute(name = SessionConst.USERNAME, required = false) == @LoginName
     */

    private final PostService postService;
    private final LoginService loginService;
    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping("/memberHome")
    public String memberHome(@ModelAttribute(name = "username") String username, Model model,
                             @CookieValue(name = "memberSearchTrg", defaultValue = "off") String searchTrg,
                             @ModelAttribute(name = "sortingDto") SortingDto sortingDto,
                             @ModelAttribute(name = "pageDto") PageDto pageDto) {
        PageControl pageControl = new PageControl(pageDto, memberService.totalCount(username));

        model.addAttribute("searchTrg", searchTrg);
        model.addAttribute("pageControl", pageControl);
        model.addAttribute("memberList", memberService.findAll(username, pageDto, sortingDto));
        return "member/memberHome";
    }

    @PostMapping("/memberHome/search")
    public String memberSearchModeChange(@CookieValue(name = "memberSearchTrg", defaultValue = "off") String searchTrg,
                                         HttpServletResponse response) {
        if (searchTrg.equals("off")) {
            Cookie memberSearchTrg = new Cookie("memberSearchTrg", "on");
            memberSearchTrg.setPath("/");
            response.addCookie(memberSearchTrg);
        }
        else if (searchTrg.equals("on")) {
            Cookie memberSearchTrg = new Cookie("memberSearchTrg", null);
            memberSearchTrg.setMaxAge(0);
            memberSearchTrg.setPath("/");
            response.addCookie(memberSearchTrg);
        }

        return "redirect:/memberHome";
    }

    @GetMapping("/signup")
    public String addMemberForm(@ModelAttribute MemberSignupDto memberDto, @ModelAttribute(name = "redirectURL") String redirectURL) {
        // 로그인 체크 후, 로그인이 되어 있지 않는 경우에만 가능하도록 추가 // 다른 웹페이지의 경우에는 그냥 안 막아놓는 것 같기도?
        return "member/addMemberForm";
    }

    @PostMapping("/signup")
    public String addMember(@Validated @ModelAttribute MemberSignupDto memberDto, BindingResult bindingResult,
                            @ModelAttribute(name = "redirectURL") String error, // 회원가입 실패 후 return 시 null 값으로 인한 에러 방지용
                            @RequestParam(defaultValue = "/memberHome") String redirectURL) {
        if (!(signupValidation(memberDto, bindingResult))) { // 회원가입 실패
            return "member/addMemberForm";
        }
        // 따로 아이디 중복 확인 기능 넣기

        memberService.addMember(new Member(memberDto.getLoginId(), memberDto.getPassword(), memberDto.getUsername()));
        return "redirect:" + redirectURL; // redirectAttributes.addFlashAttribute 로 회원가입 성공 메시지 넘기기 추가
    }

    @GetMapping("/member/{memberId}")
    public String memberDetail(@PathVariable Long memberId, Model model, @Login Long loginMemberId,
                               @ModelAttribute(name = "status") LoginStatus status, BindingResult bindingResult,
                               @RequestParam(defaultValue = "1", name = "pPage") int postPageNum,
                               @RequestParam(defaultValue = "1", name = "cPage") int commentPageNum) {
        if (status.isAccessDenied()) { // 이부분은 에러페이지로 전송하는 걸로 변경하는 것이 더 괜찮을 수도.
            globalErrorReject(bindingResult, "accessDenied.editMember", "회원수정");
        }

        PageDto postPageDto = new PageDto(postPageNum, 7);
        PageDto commentPageDto = new PageDto(commentPageNum, 7);
        PageControl postPageControl = new PageControl(postPageDto, postService.totalCountByMemberId(memberId));
        PageControl commentPageControl = new PageControl(commentPageDto, commentService.totalCountByMemberId(memberId));

        status.setAuthority(loginService.authAndAdminCheck(loginMemberId, memberId)); // 권한이 있는 회원만 회원수정 버튼이 표시되도록

        model.addAttribute("memberDetail", memberService.findByMemberId(memberId)); // 나중에 넣을 객체 DTO 로 수정 필요
        model.addAttribute("postPageControl", postPageControl);
        model.addAttribute("commentPageControl", commentPageControl);
        model.addAttribute("postList", postService.findByMemberIdAll(memberId, postPageDto)); // 나중에 넣을 객체 DTO 로 수정 필요
        model.addAttribute("commentList", commentService.findByMemberIdAll(memberId, commentPageDto)); // 나중에 넣을 객체 DTO 로 수정 필요
//        List<PostBoardDto> postDtoList = postService.postListToPostDto(postService.findByMemberIdAll(memberId));
//        List<CommentBoardDto> commentDtoList = commentService.commentListToCommentDto(commentService.findByMemberIdAll(memberId));
        return "member/memberDetail";
    }

    @GetMapping("/member/{memberId}/edit")
    public String editMemberForm(@PathVariable Long memberId, Model model, RedirectAttributes redirectAttributes,
                                 @Login Long loginMemberId) {
        // 그냥 단순히 접근할 권한이 없습니다 페이지만 띄워도 괜찮을듯
        if (!(editMemberAuthConfirm(memberId, loginMemberId, redirectAttributes))) {
            return "redirect:/member/{memberId}";
        }

        Member findMember = memberService.findByMemberId(memberId);

        model.addAttribute("memberNonEditData", findMember); //TODO ##추후 Dto 로 변경하기##
        model.addAttribute("memberEditDto", new MemberEditDto(findMember.getPassword(), findMember.getUsername()));
        return "member/editMemberForm";
    }

    @PostMapping("/member/{memberId}/edit")
    public String editMember(@PathVariable Long memberId, @Login Long loginMemberId, RedirectAttributes redirectAttributes,
                             @Validated @ModelAttribute MemberEditDto memberDto, BindingResult bindingResult, Model model) {
        if (!(editMemberAuthConfirm(memberId, loginMemberId, redirectAttributes))) {
            return "redirect:/member/{memberId}";
        }

        if (!(editMemberValidation(memberDto, memberId, bindingResult))) { // 닉네임 중복 체크 및 에러 처리
            model.addAttribute("memberNonEditData", memberService.findByMemberId(memberId)); // DTO 변경
            return "member/editMemberForm";
        }

        memberService.editMember(memberId, new Member(memberDto.getPassword(), memberDto.getUsername()));
        return "redirect:/member/{memberId}";
    }


    private boolean editMemberAuthConfirm(Long authId, Long loginMemberId, RedirectAttributes redirectAttributes) {
        if (loginService.authAndAdminCheck(loginMemberId, authId)) {
            return true; // 권한 유저 혹은 관리자
        }
        redirectAttributes.addFlashAttribute("status", new LoginStatus(true)); // 접근 권한 없음 표시
        return false; // 권한 없음
    }

    private boolean signupValidation(MemberSignupDto memberDto, BindingResult bindingResult) {
        if (memberService.loginIdDuplicateCheck(memberDto.getLoginId())) { // 로그인 아이디 중복 체크
            bindingResult.rejectValue("loginId", "loginIdDuplicate");
        }
        if (memberService.usernameDuplicateCheck(memberDto.getUsername())) { // 이름 중복 체크
            bindingResult.rejectValue("username", "loginIdDuplicate");
        }
        if (bindingResult.hasErrors()) {
            globalErrorReject(bindingResult, "failed", "회원가입");
            return false;
        }
        return true; // 에러 발생 X
    }

    private boolean editMemberValidation(MemberEditDto memberDto, Long memberId, BindingResult bindingResult) {
        if (memberService.usernameDuplicateCheck(memberDto.getUsername())) { // 이름 중복 체크
            if (!(memberDto.getUsername().equals(memberService.findByUsername(memberId)))) {
                // 기존 닉네임과 동일한 입력값인 경우 중복으로 처리하지 않도록
                bindingResult.rejectValue("username", "loginIdDuplicate");
            }
        }
        if (bindingResult.hasErrors()) {
            globalErrorReject(bindingResult, "failed", "회원수정");
            return false;
        }
        return true; // 에러 발생 X
    }

    private void globalErrorReject(BindingResult bindingResult, String errorCode, Object... errorArgs) {
        bindingResult.reject(errorCode, errorArgs, null);
    }


//    @PostConstruct
//    public void init() { // 확인용 값 추가
//        memberService.addMember(new Member("admin", "admin!", "관리자계정"));
//        memberService.addMember(new Member("test1", "pw1234", "테스트1호"));
//        memberService.addMember(new Member("test2", "pw1234", "테스트2호"));
//    }
}

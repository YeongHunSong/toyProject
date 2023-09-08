package syh.toyproject.controller.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import syh.toyproject.Dto.login.LoginDto;
import syh.toyproject.Dto.login.LoginToMainDto;
import syh.toyproject.argumentResolver.Login;
import syh.toyproject.domain.member.Member;
import syh.toyproject.service.login.LoginService;
import syh.toyproject.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) == @Login

//    @GetMapping("/main")
    public String mainPage(@Login Long loginMemberId, Model model) {
        if (loginMemberId == null) { // 세션의 회원 정보가 있는지 확인
            return "redirect:/postHome";
        }

        LoginToMainDto loginToMainDto = loginService.sessionMemberIdToLoginDto(loginMemberId);
//        model.addAttribute("loginMember", loginToMainDto);
//        return "main/mainPage";
        return "redirect:/postHome";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginDto loginDto) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginDto loginDto, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/memberHome") String redirectURI, HttpServletRequest request) {
        if (bindingResult.hasErrors()) { // 로그인 입력 확인
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginDto.getLoginId(), loginDto.getPassword());
        if (loginMember == null) { // 로그인 실패 확인
            bindingResult.reject("failed.login", null, null);
            return "login/loginForm";
        }

        // 로그인 성공 처리
        // 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember.getMemberId());
        return "redirect:" + redirectURI;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, @RequestParam(defaultValue = "/memberHome") String redirectURI) {
        HttpSession session = request.getSession(false); // false 일 경우, 세션이 없을 때는 null 만 반환.
        if (session != null) session.invalidate();

        log.info("redirectURI = {}", redirectURI);
        log.info("requestURI = {}", request.getRequestURI());

        return "redirect:" + redirectURI;

        // FIXME requestURI 받아와서 이전 화면으로 돌리기
    }
}

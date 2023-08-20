package syh.toyproject.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import syh.toyproject.service.login.LoginService;
import syh.toyproject.service.member.MemberService;
import syh.toyproject.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionMemberCheckInterceptor implements HandlerInterceptor {

    private final LoginService loginService;
    private final MemberService memberService;

    /**
     * # 로그인 여부 확인
     *      로그인한 경우, username 세션에 넣기.
     *      로그인 안 한 경우, 그냥 넘기기.
     *
     * 세션이 이상한 경우, 세션 날리기.
     *
     * 활용 용도:
     * 로그인이 필수가 아닌 페이지에서 로그인 상태를 확인하기 위함.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (session != null) {

            if (session.getAttribute(SessionConst.LOGIN_MEMBER) != null) {
                Long loginMemberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);

                if (loginService.memberCheck(loginMemberId)) {
                    String loginUsername = memberService.findByUsername(loginMemberId);

                    if (session.getAttribute("username") != null) {
                        String sessionUsername = session.getAttribute("username").toString();
                        if (sessionUsername.equals(loginUsername)) { // username 문제 없음.
                            return true;
                        } // 회원이름이 세션이름과 일치하지 않음
                    } // 회원 이름값 없음
                    // 로그인 확인 후 username 세션 없으면 닉네임 전달하기 // 추후에는 필요한 데이터가 더 있으면 그걸 전달하는 걸로.
                    session.setAttribute("username", loginUsername);
                } // 세션이 있으나 회원 목록에 없음
            } else { // 세션 내 loginMember null
                session.invalidate(); // 세션에 잘못된 정보가 들어가 있으므로 세션을 날려버림.
            }
        } // 세션 null
        return true;
    }
}

//        // 권한이 없습니다, 로그인 하세요, 등 같은 메시지 보내기
//        // request 세션에 메시지 정보 넣어서 하기
//        response.sendRedirect("/login?redirectURL=" + requestURI);

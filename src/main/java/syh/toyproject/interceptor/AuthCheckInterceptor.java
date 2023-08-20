package syh.toyproject.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import syh.toyproject.service.login.LoginService;
import syh.toyproject.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 수정 권한 확인 (본인, 혹은 ADMIN)
 */

@Slf4j
@RequiredArgsConstructor
public class AuthCheckInterceptor implements HandlerInterceptor {

    private final LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            
            log.info("권한없음"); // 아예 AccessDenied 페이지로 보낼지, 아니면 그냥 에러 메시지 한 줄 추가할지.
            return false;
        }

        Long loginMemberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);
//        loginService.authAndAdminCheck(loginMemberId, );
        // 굳이 개별적인 페이지에 포괄적인 처리로 권한 정리를 하는 게 맞을까 싶음.




            return true;
    }
}

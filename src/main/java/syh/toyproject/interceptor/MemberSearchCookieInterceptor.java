package syh.toyproject.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
@Component
public class MemberSearchCookieInterceptor implements HandlerInterceptor {

    /**
     * 검색 기능에 사용된 쿠키를 다른 페이지로 이동 시 제거하기 위함.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("memberSearchTrg"))
                .findFirst().ifPresent(cookie ->
                        expireCookie(response, "memberSearchTrg"));
        return true;
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, "off");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}

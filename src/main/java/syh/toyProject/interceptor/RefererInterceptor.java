package syh.toyProject.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
@Component
public class RefererInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String referer = request.getHeader("Referer");
        log.info("referer = {}", referer);

        if (referer != null) {
            boolean refererCookieChk = Arrays.stream(request.getCookies())
                    .anyMatch(cookie -> cookie.getName().equals("refererCookie"));
            if (!refererCookieChk) {
                Cookie refererCookie = new Cookie("referer", referer);
                refererCookie.setPath("/"); // 이것도 설정 하는 걸로
                response.addCookie(refererCookie);
                
            }
                // referer 세션이 있는 경우에는 담지 않는 걸로
            Cookie refrerCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("referer"))
                    .findFirst().orElse(new Cookie("referer", "값이업서요"));


            log.info("cookie.referer = {}", refrerCookie.getValue());
        }
        return true;
    }
}

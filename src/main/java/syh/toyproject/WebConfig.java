package syh.toyproject;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import syh.toyproject.argumentResolver.LoginMemberArgumentResolver;
import syh.toyproject.argumentResolver.LoginNameArgumentResolver;
import syh.toyproject.interceptor.MemberSearchCookieInterceptor;
import syh.toyproject.interceptor.PostSearchCookieInterceptor;
import syh.toyproject.interceptor.RefererInterceptor;
import syh.toyproject.interceptor.SessionMemberCheckInterceptor;

import java.util.List;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SessionMemberCheckInterceptor sessionMemberCheckInterceptor;
    private final MemberSearchCookieInterceptor memberSearchCookieInterceptor;
    private final PostSearchCookieInterceptor postSearchCookieInterceptor;
    private final RefererInterceptor refererInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LogInterceptor())
//                .order(1)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(sessionMemberCheckInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**", "/*.ico", "/error",
                        "/member/*/edit", "/post/*/edit", "/post/*/*/edit");

        registry.addInterceptor(memberSearchCookieInterceptor)
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**", "/*.ico", "/error",
                        "/memberHome/**");

        registry.addInterceptor(postSearchCookieInterceptor)
                .order(3)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error",
                        "/postHome/**");

//        registry.addInterceptor(refererInterceptor)
//                .order(4)
//                .addPathPatterns("/login", "/signup", "/logout", "/")
//                .excludePathPatterns("/css/**", "/*.ico", "/error");
    }


    /**
     * @Login
     * @LoginName
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
        resolvers.add(new LoginNameArgumentResolver());
    }
}

/*
수정 권한이 필요한 링크 (본인, ADMIN)
/member/{memberId}/edit
/post/{postId}/edit
/post/{postId}/{commentId}/edit


 */

/* PathPattern

?  한 문자 일치

* 경로 (/) 안에서 0개 이상의 문자 일치
** 경로 끝까지 0개 이상의 경로 (/) 일치

{spring} 경로(/)와 일치하고 spring 이라는 변수로 캡처
{spring:[a-z]+} regexp [a-z] 와 일치하고, "spring" 경로 변수로 캡처
{*spring} 경로가 끝날 때까지 0개 이상의 경로(/)와 일치하고 spring 이라는 변수로 캡처

sample

/pages/t?st.html -> matches /pages/test.html, /pages/tXst.html but not /pages/toast.html

/resources/*.png -> matches all .png files in the resources directory

/resources/** -> matches all files underneath the /resources/ path, including /resources/images.png and /resources/css/spring.css

/resources/{*path} -> matches all files underneath the /resources/ path and captures their relative path in a variable named "path";

/resources/image.png will match with "path" -> "/image.png, and /resources/css/spring.css will match with "path" -> "/css/spring.css"

/resources/{filename:\\w+}.dat will match with /resources/spring.dat and assign the value "spring" to the filename variable

 */
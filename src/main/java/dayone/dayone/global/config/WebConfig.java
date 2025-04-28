package dayone.dayone.global.config;

import dayone.dayone.auth.ui.argumentresolver.AuthArgumentResolver;
import dayone.dayone.auth.ui.interceptor.AuthenticationInterceptor;
import dayone.dayone.auth.ui.interceptor.AuthorizationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthorizationInterceptor authorizationInterceptor;
    private final AuthArgumentResolver authArgumentResolver;

    @Override
    public void addInterceptors(final org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/v1/admin/**")
            .excludePathPatterns("/api/v1/auth/login")
            .excludePathPatterns("/api/v1/auth/reissue-token");

        registry.addInterceptor(authorizationInterceptor)
            .addPathPatterns("/api/v1/admin/**")
            .excludePathPatterns("/api/v1/auth/login")
            .excludePathPatterns("/api/v1/auth/reissue-token");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        // TODO: 추후에 실제 도메인으로 변경할 것
        registry.addMapping("/**") // 모든 경로 허용
            .allowedOrigins("http://localhost:3000") // 모든 웹 사이트 허용
            .allowedMethods("*") // 허용할 HTTP 메서드
            .allowedHeaders("*") // 모든 헤더 허용
            .allowCredentials(true)
            .exposedHeaders("Location");
    }
}

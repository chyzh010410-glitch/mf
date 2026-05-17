package com.mf.fertilizer.config;

import com.mf.fertilizer.constant.RedisKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class JwtInterceptorConfig implements WebMvcConfigurer {

    private final StringRedisTemplate redisTemplate;

    private static final String CLIENT_TOKEN_PREFIX = "client:token:";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor(redisTemplate))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login", "/logout",
                        "/client/auth/login", "/client/auth/register", "/client/auth/captcha", "/client/auth/reset-password",
                        "/client/products/**", "/client/categories/**",
                        "/client/encyclopedia/**", "/client/articles/**",
                        "/client/home", "/client/faq/**", "/client/activities/**",
                        "/doc.html", "/webjars/**", "/v3/api-docs/**", "/swagger-resources/**", "/error"
                );
    }

    record JwtInterceptor(StringRedisTemplate redisTemplate) implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            var token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                response.setStatus(401);
                return false;
            }
            token = token.substring(7);
            String path = request.getRequestURI();
            // Determine token prefix based on path
            String keyPrefix = path.startsWith("/client/") ? CLIENT_TOKEN_PREFIX : RedisKey.LOGIN_TOKEN;
            var exists = redisTemplate.hasKey(keyPrefix + token);
            if (Boolean.FALSE.equals(exists)) {
                response.setStatus(401);
                return false;
            }
            return true;
        }
    }
}

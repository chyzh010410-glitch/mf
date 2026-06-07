package com.mf.server.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final ReactiveStringRedisTemplate redisTemplate;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final SecretKey KEY = Keys.hmacShaKeyFor(
            "miaoFeiFertilizerJwtSecretKey2026!@#%".getBytes(StandardCharsets.UTF_8));
    private static final String CLIENT_TOKEN_PREFIX = "client:token:";
    private static final String LOGIN_TOKEN_PREFIX = "login:token:";

    private static final List<String> EXCLUDE_PATHS = List.of(
            "/login", "/logout",
            "/client/auth/login", "/client/auth/register", "/client/auth/captcha", "/client/auth/reset-password",
            "/client/products/**", "/client/categories/**",
            "/client/encyclopedia/**", "/client/articles/**",
            "/client/home", "/client/faq/**", "/client/activities/**",
            "/doc.html", "/webjars/**", "/v3/api-docs/**", "/swagger-resources/**", "/error", "/favicon.ico"
    );

    public JwtAuthFilter(ReactiveStringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        for (String pattern : EXCLUDE_PATHS) {
            if (pathMatcher.match(pattern, path)) {
                return chain.filter(exchange);
            }
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parser().verifyWith(KEY).build()
                    .parseSignedClaims(token).getPayload();

            String keyPrefix = path.startsWith("/client/") ? CLIENT_TOKEN_PREFIX : LOGIN_TOKEN_PREFIX;

            // 用户信息放入请求头
            var newExchange = exchange.mutate()
                    .request(r -> r.header("X-User-Id", claims.getId())
                            .header("X-Username", claims.getSubject())
                            .header("X-Role", claims.get("role", String.class))
                            .header("X-User-Type", claims.get("userType", String.class)))
                    .build();

            // 响应式校验 Redis token
            return redisTemplate.hasKey(keyPrefix + token)
                    .flatMap(exists -> {
                        if (Boolean.TRUE.equals(exists)) {
                            return chain.filter(newExchange);
                        }
                        newExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return newExchange.getResponse().setComplete();
                    });

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}

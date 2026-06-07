package com.mf.server.filter;
import com.mf.common.context.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component @Order(-200)
public class UserContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String userId = ((HttpServletRequest) req).getHeader("X-User-Id");
        String username = ((HttpServletRequest) req).getHeader("X-Username");
        if (userId != null) UserContext.set(Long.valueOf(userId), username);
        try { chain.doFilter(req, res); } finally { UserContext.clear(); }
    }
}

package com.mf.fertilizer.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mf.fertilizer.annotation.OperationLog;
import com.mf.fertilizer.context.UserContext;
import com.mf.fertilizer.entity.SystemLog;
import com.mf.fertilizer.service.SystemLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class OperationLogAspect {

    private final SystemLogService systemLogService;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Around("@annotation(opLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog opLog) throws Throwable {
        var sysLog = new SystemLog();
        sysLog.setModule(opLog.module());
        sysLog.setAction(opLog.action());
        sysLog.setTarget(opLog.target());
        sysLog.setCreateTime(LocalDateTime.now());

        // 操作人信息 from ThreadLocal
        Long userId = UserContext.getUserId();
        if (userId != null) {
            sysLog.setOperatorId(userId);
            sysLog.setOperatorName(UserContext.getUsername());
        }

        // 请求参数
        try {
            Object[] args = joinPoint.getArgs();
            // 过滤掉 HttpServletRequest/Response 类型的参数
            var filtered = new java.util.ArrayList<>();
            if (args != null) {
                for (Object arg : args) {
                    if (arg == null) continue;
                    if (arg instanceof jakarta.servlet.http.HttpServletRequest) continue;
                    if (arg instanceof jakarta.servlet.http.HttpServletResponse) continue;
                    filtered.add(arg);
                }
            }
            if (!filtered.isEmpty()) {
                sysLog.setRequestParams(MAPPER.writeValueAsString(filtered));
            }
        } catch (Exception e) {
            sysLog.setRequestParams("[序列化失败]");
        }

        // IP 和 User-Agent
        try {
            var attrs = RequestContextHolder.getRequestAttributes();
            if (attrs instanceof ServletRequestAttributes servletAttrs) {
                HttpServletRequest request = servletAttrs.getRequest();
                sysLog.setIp(getClientIp(request));
                sysLog.setUserAgent(request.getHeader("User-Agent"));
            }
        } catch (Exception ignored) {}

        long start = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
            sysLog.setCostTime(System.currentTimeMillis() - start);
            sysLog.setResult("success");
            return result;
        } catch (Throwable e) {
            sysLog.setCostTime(System.currentTimeMillis() - start);
            sysLog.setResult("error");
            sysLog.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            try {
                systemLogService.save(sysLog);
            } catch (Exception e) {
                log.warn("Failed to save operation log: {}", e.getMessage());
            }
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip != null && ip.contains(",") ? ip.split(",")[0].trim() : ip;
    }
}

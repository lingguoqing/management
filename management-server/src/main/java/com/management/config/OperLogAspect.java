package com.management.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONUtil;
import com.management.common.annotation.OperLog;
import com.management.entity.SysOperationLog;
import com.management.service.SysOperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * AOP aspect — intercepts @OperLog methods and records operation logs to the database
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperLogAspect {

    private final SysOperationLogService logService;
    private final HttpServletRequest request;

    @Around("@annotation(operLog)")
    public Object around(ProceedingJoinPoint point, OperLog operLog) throws Throwable {
        SysOperationLog logEntry = new SysOperationLog();

        // ---- capture request info BEFORE execution ----
        try {
            Object loginId = StpUtil.getLoginIdDefaultNull();
            if (loginId != null) {
                logEntry.setUserId(Long.valueOf(loginId.toString()));
            }
        } catch (Exception ignored) { /* not logged in */ }

        logEntry.setModule(operLog.module());
        logEntry.setOperation(operLog.operation());
        logEntry.setRequestUrl(request.getRequestURI());
        logEntry.setRequestMethod(request.getMethod());
        logEntry.setIp(getClientIp(request));

        MethodSignature signature = (MethodSignature) point.getSignature();
        logEntry.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());

        // request params (skip large binary data)
        Object[] args = point.getArgs();
        try {
            if (args != null && args.length > 0) {
                String json = JSONUtil.toJsonStr(args);
                logEntry.setRequestParam(json.length() > 2000 ? json.substring(0, 2000) : json);
            }
        } catch (Exception ignored) { /* serialization error */ }

        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = point.proceed();
            long cost = System.currentTimeMillis() - start;
            logEntry.setCostTime(cost);
            logEntry.setStatus(1);
            return result;
        } catch (Throwable e) {
            long cost = System.currentTimeMillis() - start;
            logEntry.setCostTime(cost);
            logEntry.setStatus(0);
            logEntry.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            // persist async-best-effort — don't let log failure break the request
            try {
                logService.save(logEntry);
            } catch (Exception e) {
                log.warn("Failed to save operation log: {}", e.getMessage());
            }
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}

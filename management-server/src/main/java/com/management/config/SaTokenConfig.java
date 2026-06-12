package com.management.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 权限认证配置
 * 配置路由拦截规则，对指定接口进行登录校验和权限校验
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 校验登录状态，同时通过注解 @SaCheckPermission 校验权限
                    StpUtil.checkLogin();
                }))
                .addPathPatterns("/api/**")
                // 排除登录相关接口
                .excludePathPatterns("/api/auth/login", "/api/auth/captcha")
                // 排除文档接口
                .excludePathPatterns("/doc.html", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/webjars/**");
    }
}

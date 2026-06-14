package com.management.controller;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.management.common.Result;
import com.management.common.annotation.OperLog;
import com.management.common.exception.BusinessException;
import com.management.model.dto.LoginDTO;
import com.management.model.dto.PasswordDTO;
import com.management.model.dto.UserDTO;
import com.management.model.entity.SysLoginLog;
import com.management.model.entity.SysUser;
import com.management.service.SysLoginLogService;
import com.management.service.SysUserService;
import com.management.model.vo.LoginVO;
import com.management.model.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 认证控制器 —— 登录 / 登出 / 获取用户信息
 */
@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SysUserService userService;
    private final SysLoginLogService loginLogService;
    private final HttpServletRequest request;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        SysLoginLog log = new SysLoginLog();
        log.setUsername(dto.getUsername());
        log.setLoginTime(LocalDateTime.now());
        log.setIp(getClientIp());

        // Parse User-Agent for browser / OS
        String ua = request.getHeader("User-Agent");
        if (ua != null) {
            try {
                UserAgent agent = UserAgentUtil.parse(ua);
                log.setBrowser(agent.getBrowser().getName());
                log.setOs(agent.getOs().getName());
            } catch (Exception ignored) { /* parse failure */ }
        }

        try {
            SysUser user = userService.getOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUser>()
                            .eq(SysUser::getUsername, dto.getUsername()));
            if (user == null) {
                log.setStatus(0);
                log.setMsg("用户名或密码错误");
                loginLogService.save(log);
                throw new BusinessException("用户名或密码错误");
            }
            if (user.getStatus() == 0) {
                log.setStatus(0);
                log.setMsg("账号已被停用");
                loginLogService.save(log);
                throw new BusinessException("账号已被停用，请联系管理员");
            }

            if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
                log.setStatus(0);
                log.setMsg("用户名或密码错误");
                loginLogService.save(log);
                throw new BusinessException("用户名或密码错误");
            }

            StpUtil.login(user.getId());
            String token = StpUtil.getTokenValue();

            log.setUserId(user.getId());
            log.setStatus(1);
            log.setMsg("登录成功");
            loginLogService.save(log);

            return Result.ok("登录成功", LoginVO.builder().token(token).build());
        } catch (BusinessException e) {
            throw e; // already logged above
        }
    }

    private String getClientIp() {
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

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.ok();
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/user-info")
    public Result<UserInfoVO> userInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        UserInfoVO vo = userService.getCurrentUserInfo(userId);
        return Result.ok(vo);
    }

    @Operation(summary = "修改密码")
    @OperLog(module = "系统管理", operation = "修改密码")
    @PutMapping("/password")
    public Result<Void> updatePassword(@Valid @RequestBody PasswordDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        userService.updatePassword(userId, dto);
        return Result.ok();
    }

    @Operation(summary = "修改个人资料")
    @OperLog(module = "系统管理", operation = "修改个人资料")
    @PutMapping("/profile")
    public Result<Void> updateProfile(@Valid @RequestBody UserDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        userService.updateProfile(userId, dto);
        return Result.ok();
    }
}

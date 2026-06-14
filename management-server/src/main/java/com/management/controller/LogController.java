package com.management.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.management.common.PageResult;
import com.management.common.PageUtils;
import com.management.common.Result;
import com.management.model.dto.query.LoginLogQueryDTO;
import com.management.model.dto.query.OperationLogQueryDTO;
import com.management.model.entity.SysOperationLog;
import com.management.model.entity.SysLoginLog;
import com.management.service.SysOperationLogService;
import com.management.service.SysLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 日志管理控制器
 */
@Tag(name = "日志管理")
@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogController {

    private final SysOperationLogService operationLogService;
    private final SysLoginLogService loginLogService;

    @Operation(summary = "分页查询操作日志")
    @PostMapping("/operation/page")
    @SaCheckPermission("sys:log:operation")
    public Result<PageResult<SysOperationLog>> pageOperation(@RequestBody OperationLogQueryDTO query) {
        return Result.ok(PageUtils.convert(operationLogService.pageLogs(query)));
    }

    @Operation(summary = "清空操作日志")
    @DeleteMapping("/operation")
    @SaCheckPermission("sys:log:operation")
    public Result<Void> clearOperation() {
        operationLogService.clearLogs();
        return Result.ok();
    }

    @Operation(summary = "分页查询登录日志")
    @PostMapping("/login/page")
    @SaCheckPermission("sys:log:login")
    public Result<PageResult<SysLoginLog>> pageLogin(@RequestBody LoginLogQueryDTO query) {
        return Result.ok(PageUtils.convert(loginLogService.pageLogs(query)));
    }

    @Operation(summary = "清空登录日志")
    @DeleteMapping("/login")
    @SaCheckPermission("sys:log:login")
    public Result<Void> clearLogin() {
        loginLogService.clearLogs();
        return Result.ok();
    }
}

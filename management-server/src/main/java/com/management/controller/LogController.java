package com.management.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.common.PageResult;
import com.management.common.Result;
import com.management.dto.QueryDTO;
import com.management.entity.SysOperationLog;
import com.management.entity.SysLoginLog;
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
    @GetMapping("/operation/page")
    @SaCheckPermission("sys:log:operation")
    public Result<PageResult<SysOperationLog>> pageOperation(QueryDTO query) {
        IPage<SysOperationLog> page = operationLogService.pageLogs(query);
        return Result.ok(PageResult.of((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal(), page.getRecords()));
    }

    @Operation(summary = "清空操作日志")
    @DeleteMapping("/operation")
    @SaCheckPermission("sys:log:operation")
    public Result<Void> clearOperation() {
        operationLogService.clearLogs();
        return Result.ok();
    }

    @Operation(summary = "分页查询登录日志")
    @GetMapping("/login/page")
    @SaCheckPermission("sys:log:login")
    public Result<PageResult<SysLoginLog>> pageLogin(QueryDTO query) {
        IPage<SysLoginLog> page = loginLogService.pageLogs(query);
        return Result.ok(PageResult.of((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal(), page.getRecords()));
    }

    @Operation(summary = "清空登录日志")
    @DeleteMapping("/login")
    @SaCheckPermission("sys:log:login")
    public Result<Void> clearLogin() {
        loginLogService.clearLogs();
        return Result.ok();
    }
}

package com.management.controller;

import com.management.common.Result;
import com.management.mapper.DashboardMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 仪表盘控制器
 */
@Tag(name = "仪表盘")
@RestController
@RequestMapping("dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardMapper dashboardMapper;

    @Operation(summary = "获取统计信息")
    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        Map<String, Object> data = new HashMap<>();
        data.put("userCount", dashboardMapper.countUsers());
        data.put("roleCount", dashboardMapper.countRoles());
        data.put("permCount", dashboardMapper.countPermissions());
        data.put("todayOperationCount", dashboardMapper.countTodayOperations());
        return Result.ok(data);
    }
}
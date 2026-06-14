package com.management.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.management.common.Result;
import com.management.common.annotation.OperLog;
import com.management.dto.DeptDTO;
import com.management.service.SysDeptService;
import com.management.vo.DeptTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("/api/dept")
@RequiredArgsConstructor
public class DeptController {

    private final SysDeptService deptService;

    @Operation(summary = "获取部门树")
    @GetMapping("/tree")
    @SaCheckPermission("sys:dept:list")
    public Result<List<DeptTreeVO>> tree() {
        return Result.ok(deptService.getDeptTree());
    }

    @Operation(summary = "新增部门")
    @OperLog(module = "部门管理", operation = "新增")
    @PostMapping
    @SaCheckPermission("sys:dept:add")
    public Result<Void> create(@Valid @RequestBody DeptDTO dto) {
        deptService.createDept(dto);
        return Result.ok();
    }

    @Operation(summary = "修改部门")
    @OperLog(module = "部门管理", operation = "修改")
    @PutMapping
    @SaCheckPermission("sys:dept:edit")
    public Result<Void> update(@Valid @RequestBody DeptDTO dto) {
        deptService.updateDept(dto);
        return Result.ok();
    }

    @Operation(summary = "删除部门")
    @OperLog(module = "部门管理", operation = "删除")
    @DeleteMapping("/{id}")
    @SaCheckPermission("sys:dept:delete")
    public Result<Void> delete(@PathVariable Long id) {
        deptService.deleteDept(id);
        return Result.ok();
    }
}

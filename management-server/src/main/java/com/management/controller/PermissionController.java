package com.management.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.management.common.Result;
import com.management.common.annotation.OperLog;
import com.management.model.dto.PermissionDTO;
import com.management.service.SysPermissionService;
import com.management.model.vo.PermTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限 / 菜单管理控制器
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("permission")
@RequiredArgsConstructor
public class PermissionController {

    private final SysPermissionService permissionService;

    @Operation(summary = "获取权限树（角色分配权限用）")
    @GetMapping("/tree")
    @SaCheckPermission("sys:perm:list")
    public Result<List<PermTreeVO>> tree() {
        return Result.ok(permissionService.getPermTree());
    }

    @Operation(summary = "获取单个权限详情")
    @GetMapping("/{id}")
    @SaCheckPermission("sys:perm:list")
    public Result<PermissionDTO> get(@PathVariable Long id) {
        return Result.ok(permissionService.getPermissionById(id));
    }

    @Operation(summary = "新增权限/菜单")
    @OperLog(module = "菜单管理", operation = "新增")
    @PostMapping
    @SaCheckPermission("sys:perm:add")
    public Result<Void> create(@Valid @RequestBody PermissionDTO dto) {
        permissionService.createPermission(dto);
        return Result.ok();
    }

    @Operation(summary = "修改权限/菜单")
    @OperLog(module = "菜单管理", operation = "修改")
    @PutMapping
    @SaCheckPermission("sys:perm:edit")
    public Result<Void> update(@Valid @RequestBody PermissionDTO dto) {
        permissionService.updatePermission(dto);
        return Result.ok();
    }

    @Operation(summary = "删除权限/菜单（级联删除子节点）")
    @OperLog(module = "菜单管理", operation = "删除")
    @DeleteMapping("/{id}")
    @SaCheckPermission("sys:perm:delete")
    public Result<Void> delete(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return Result.ok();
    }
}

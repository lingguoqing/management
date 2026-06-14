package com.management.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.common.PageResult;
import com.management.common.PageUtils;
import com.management.common.Result;
import com.management.common.annotation.OperLog;
import com.management.dto.RoleDTO;
import com.management.dto.query.RoleQueryDTO;
import com.management.entity.SysRole;
import com.management.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final SysRoleService roleService;

    @Operation(summary = "分页查询角色列表")
    @PostMapping("/page")
    @SaCheckPermission("sys:role:list")
    public Result<PageResult<SysRole>> page(@RequestBody RoleQueryDTO query) {
        return Result.ok(PageUtils.convert(roleService.pageRoles(query)));
    }

    @Operation(summary = "新增角色")
    @OperLog(module = "角色管理", operation = "新增")
    @PostMapping
    @SaCheckPermission("sys:role:add")
    public Result<Void> create(@Valid @RequestBody RoleDTO dto) {
        roleService.createRole(dto);
        return Result.ok();
    }

    @Operation(summary = "修改角色")
    @OperLog(module = "角色管理", operation = "修改")
    @PutMapping
    @SaCheckPermission("sys:role:edit")
    public Result<Void> update(@Valid @RequestBody RoleDTO dto) {
        roleService.updateRole(dto);
        return Result.ok();
    }

    @Operation(summary = "删除角色")
    @OperLog(module = "角色管理", operation = "删除")
    @DeleteMapping("/{id}")
    @SaCheckPermission("sys:role:delete")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.ok();
    }

    @Operation(summary = "查询所有角色（下拉列表用）")
    @GetMapping("/list")
    public Result<List<SysRole>> list() {
        return Result.ok(roleService.listAll());
    }

    @Operation(summary = "获取角色的权限ID列表")
    @GetMapping("/{id}/perm-ids")
    @SaCheckPermission("sys:role:assign-perm")
    public Result<List<Long>> getPermIds(@PathVariable Long id) {
        return Result.ok(roleService.getRolePermIds(id));
    }

    @Operation(summary = "分配角色权限")
    @OperLog(module = "角色管理", operation = "分配权限")
    @PutMapping("/{id}/permissions")
    @SaCheckPermission("sys:role:assign-perm")
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody Long[] permIds) {
        roleService.assignPermissions(id, permIds);
        return Result.ok();
    }
}

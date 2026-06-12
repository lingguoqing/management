package com.management.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.common.PageResult;
import com.management.common.Result;
import com.management.common.annotation.OperLog;
import com.management.dto.QueryDTO;
import com.management.dto.UserDTO;
import com.management.entity.SysUser;
import com.management.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final SysUserService userService;

    @Operation(summary = "分页查询用户列表")
    @GetMapping("/page")
    @SaCheckPermission("sys:user:list")
    public Result<PageResult<SysUser>> page(QueryDTO query) {
        IPage<SysUser> page = userService.pageUsers(query);
        return Result.ok(PageResult.of((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal(), page.getRecords()));
    }

    @Operation(summary = "新增用户")
    @OperLog(module = "用户管理", operation = "新增")
    @PostMapping
    @SaCheckPermission("sys:user:add")
    public Result<Void> create(@Valid @RequestBody UserDTO dto) {
        userService.createUser(dto);
        return Result.ok();
    }

    @Operation(summary = "修改用户")
    @OperLog(module = "用户管理", operation = "修改")
    @PutMapping
    @SaCheckPermission("sys:user:edit")
    public Result<Void> update(@Valid @RequestBody UserDTO dto) {
        userService.updateUser(dto);
        return Result.ok();
    }

    @Operation(summary = "删除用户")
    @OperLog(module = "用户管理", operation = "删除")
    @DeleteMapping("/{id}")
    @SaCheckPermission("sys:user:delete")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.ok();
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    @SaCheckPermission("sys:user:list")
    public Result<SysUser> detail(@PathVariable Long id) {
        return Result.ok(userService.getById(id));
    }
}

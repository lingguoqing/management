package com.management.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.management.common.PageResult;
import com.management.common.PageUtils;
import com.management.common.Result;
import com.management.common.annotation.OperLog;
import com.management.model.dto.UserDTO;
import com.management.model.dto.query.UserQueryDTO;
import com.management.model.entity.SysUser;
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
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final SysUserService userService;

    @Operation(summary = "分页查询用户列表")
    @PostMapping("/page")
    @SaCheckPermission("sys:user:list")
    public Result<PageResult<SysUser>> page(@RequestBody UserQueryDTO query) {
        return Result.ok(PageUtils.convert(userService.pageUsers(query)));
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
    public Result<UserDTO> detail(@PathVariable Long id) {
        return Result.ok(userService.getUserDetail(id));
    }
}

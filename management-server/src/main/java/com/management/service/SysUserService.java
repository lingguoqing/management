package com.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.model.dto.PasswordDTO;
import com.management.model.dto.UserDTO;
import com.management.model.dto.query.UserQueryDTO;
import com.management.model.entity.SysUser;
import com.management.model.vo.UserInfoVO;

/**
 * 用户 Service 接口
 */
public interface SysUserService extends IService<SysUser> {

    /** 分页查询用户列表 */
    IPage<SysUser> pageUsers(UserQueryDTO query);

    /** 新增用户（含角色关联） */
    void createUser(UserDTO dto);

    /** 修改用户（含角色关联） */
    void updateUser(UserDTO dto);

    /** 删除用户（逻辑删除） */
    void deleteUser(Long id);

    /** 获取当前登录用户信息（含权限/菜单） */
    UserInfoVO getCurrentUserInfo(Long userId);

    /** 修改密码 */
    void updatePassword(Long userId, PasswordDTO dto);

    /** 修改个人资料（只能修改自己） */
    void updateProfile(Long userId, UserDTO dto);

    /** 获取用户详情（含角色ID列表） */
    UserDTO getUserDetail(Long id);
}

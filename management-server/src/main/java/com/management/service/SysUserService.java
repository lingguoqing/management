package com.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.dto.PasswordDTO;
import com.management.dto.QueryDTO;
import com.management.dto.UserDTO;
import com.management.entity.SysUser;
import com.management.vo.UserInfoVO;

/**
 * 用户 Service 接口
 */
public interface SysUserService extends IService<SysUser> {

    /** 分页查询用户列表 */
    IPage<SysUser> pageUsers(QueryDTO query);

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
}

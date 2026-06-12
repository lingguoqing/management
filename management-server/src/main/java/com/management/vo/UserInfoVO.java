package com.management.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 当前登录用户信息 VO
 */
@Data
public class UserInfoVO {

    /** 用户ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 头像 */
    private String avatar;

    /** 邮箱 */
    private String email;

    /** 手机号 */
    private String phone;

    /** 性别 */
    private Integer gender;

    /** 部门ID */
    private Long deptId;

    /** 部门名称 */
    private String deptName;

    /** 状态 */
    private Integer status;

    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;

    /** 角色编码列表 */
    private List<String> roles;

    /** 权限标识列表（如 sys:user:add） */
    private Set<String> permissions;

    /** 用户可见的菜单树 */
    private List<MenuVO> menus;
}

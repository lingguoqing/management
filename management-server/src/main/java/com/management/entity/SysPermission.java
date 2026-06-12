package com.management.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限 / 菜单实体 —— 通过 parent_id 构建菜单树
 * perm_type: 1-目录 2-菜单 3-按钮
 * 按钮权限由 perm_code 标识（如 sys:user:add）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_permission")
public class SysPermission extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 父权限ID（0=顶级） */
    private Long parentId;

    /** 权限名称 */
    private String permName;

    /** 权限标识（如 sys:user:add），按钮权限必填 */
    private String permCode;

    /** 类型：1-目录 2-菜单 3-按钮 */
    private Integer permType;

    /** 路由路径（菜单类型必填） */
    private String path;

    /** 前端组件路径（菜单类型必填） */
    private String component;

    /** 菜单图标（Ant Design 图标名称） */
    private String icon;

    /** 排序号 */
    private Integer sortOrder;

    /** 是否可见：0-隐藏 1-显示 */
    private Integer visible;

    /** 是否缓存（keep-alive）：0-否 1-是 */
    private Integer keepAlive;

    /** 状态：0-停用 1-正常 */
    private Integer status;
}

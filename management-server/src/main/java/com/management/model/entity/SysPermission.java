package com.management.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单权限表
 * @TableName sys_permission
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="sys_permission")
@Data
public class SysPermission extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 6387369911402103199L;
    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID（0为根）
     */
    private Long parentId;

    /**
     * 路由名称/组件名（前端keep-alive按此缓存）
     */
    private String name;

    /**
     * 菜单显示标题（侧边栏/面包屑/i18n key）
     */
    private String title;

    /**
     * 英文标题/i18n键
     */
    private String enName;

    /**
     * 路由地址，如 system/user
     */
    private String path;

    /**
     * 前端组件路径，如 system/user/index；Layout 表示父布局
     */
    private String component;

    /**
     * 一级菜单重定向地址
     */
    private String redirect;

    /**
     * 后端接口路径（兼容旧版本）
     */
    private String url;

    /**
     * 菜单类型：0=目录 1=菜单 2=按钮
     */
    private Integer type;

    /**
     * 权限标识，如 system:user:add
     */
    private String permission;

    /**
     * 权限策略：1=可见 2=禁用（按钮级）
     */
    private String permsType;

    /**
     * 菜单图标（iconfont / svg / 组件名）
     */
    private String icon;

    /**
     * 排序值，越小越靠前
     */
    private Integer sort;

    /**
     * 是否显示：0=隐藏 1=显示
     */
    private Integer visible;

    /**
     * 是否注册为路由：0=否 1=是
     */
    private Integer isRoute;

    /**
     * 是否叶子节点：0=否 1=是
     */
    private Integer isLeaf;

    /**
     * 聚合子路由：0=否 1=是
     */
    private Integer alwaysShow;

    /**
     * 是否缓存页面：0=否 1=是
     */
    private Integer keepAlive;

    /**
     * 是否外链：0=内部 1=外部打开
     */
    private Integer iFrame;

    /**
     * 是否隐藏tab：0=否 1=是
     */
    private Integer hideTab;

    /**
     * 菜单/按钮状态：0=停用 1=启用
     */
    private Integer status;

    /**
     * 子菜单数（冗余，避免COUNT）
     */
    private Integer subCount;

    /**
     * 备注/描述
     */
    private String description;

    /**
     * 是否带数据权限：0=否 1=是
     */
    private Integer ruleFlag;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;
}
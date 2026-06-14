package com.management.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 前端菜单 / 路由 VO
 */
@Data
public class MenuVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 父菜单ID */
    private Long parentId;

    /** 路由名称/组件名 */
    private String name;

    /** 菜单显示标题 */
    private String title;

    /** 路由地址 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 重定向地址 */
    private String redirect;

    /** 菜单图标 */
    private String icon;

    /** 菜单类型：0=目录 1=菜单 2=按钮 */
    private Integer type;

    /** 权限标识 */
    private String permission;

    /** 是否显示：0=隐藏 1=显示 */
    private Integer visible;

    /** 是否缓存页面：0=否 1=是 */
    private Integer keepAlive;

    /** 是否外链：0=内部 1=外部打开 */
    private Integer iFrame;

    /** 是否隐藏tab：0=否 1=是 */
    private Integer hideTab;

    /** 聚合子路由：0=否 1=是 */
    private Integer alwaysShow;

    /** 排序值 */
    private Integer sort;

    /** 子菜单 */
    private List<MenuVO> children;
}
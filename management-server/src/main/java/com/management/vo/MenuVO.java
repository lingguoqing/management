package com.management.vo;

import lombok.Data;

import java.util.List;

/**
 * 前端菜单 / 路由 VO
 */
@Data
public class MenuVO {

    private Long id;

    private Long parentId;

    /** 菜单名称 */
    private String permName;

    /** 路由路径 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 图标 */
    private String icon;

    /** 类型：1-目录 2-菜单 3-按钮 */
    private Integer permType;

    /** 是否可见 */
    private Integer visible;

    /** 是否缓存 */
    private Integer keepAlive;

    /** 排序号 */
    private Integer sortOrder;

    /** 子菜单 */
    private List<MenuVO> children;
}

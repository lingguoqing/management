package com.management.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 权限树 VO —— 用于角色分配权限时的树形选择
 */
@Data
public class PermTreeVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 父菜单ID */
    private Long parentId;

    /** 路由名称/组件名 */
    private String name;

    /** 菜单显示标题 */
    private String title;

    /** 英文标题 */
    private String enName;

    /** 菜单类型：0=目录 1=菜单 2=按钮 */
    private Integer type;

    /** 权限标识 */
    private String permission;

    /** 菜单图标 */
    private String icon;

    /** 排序值 */
    private Integer sort;

    /** 是否显示：0=隐藏 1=显示 */
    private Integer visible;

    /** 菜单/按钮状态：0=停用 1=启用 */
    private Integer status;

    /** 子节点 */
    private List<PermTreeVO> children;
}
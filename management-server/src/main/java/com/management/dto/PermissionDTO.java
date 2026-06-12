package com.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 权限 / 菜单 DTO
 */
@Data
public class PermissionDTO {

    private Long id;

    private Long parentId;

    @NotBlank(message = "权限名称不能为空")
    private String permName;

    /** 权限标识（按钮权限必填） */
    private String permCode;

    @NotNull(message = "权限类型不能为空")
    private Integer permType;

    /** 路由路径 */
    private String path;

    /** 前端组件路径 */
    private String component;

    /** 图标 */
    private String icon;

    private Integer sortOrder;

    private Integer visible;

    private Integer keepAlive;

    private Integer status;
}

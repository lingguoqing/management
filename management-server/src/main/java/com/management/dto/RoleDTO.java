package com.management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 角色 DTO
 */
@Data
public class RoleDTO {

    private Long id;

    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    private String roleDesc;

    /** 数据权限范围 */
    private Integer dataScope;

    private Integer sortOrder;

    private Integer status;

    /** 关联的权限ID列表 */
    private Long[] permIds;
}

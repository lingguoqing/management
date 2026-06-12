package com.management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 部门 DTO
 */
@Data
public class DeptDTO {

    private Long id;

    private Long parentId;

    @NotBlank(message = "部门名称不能为空")
    private String deptName;

    @NotBlank(message = "部门编码不能为空")
    private String deptCode;

    private Integer sortOrder;

    private String leader;

    private String phone;

    private String email;

    private Integer status;
}

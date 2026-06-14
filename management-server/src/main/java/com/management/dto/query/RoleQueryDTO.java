package com.management.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleQueryDTO extends PageQuery {

    /** 关键词（角色名/角色编码） */
    private String keyword;

    /** 状态（0-禁用，1-正常） */
    private Integer status;
}
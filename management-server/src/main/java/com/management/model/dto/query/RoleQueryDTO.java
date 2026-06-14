package com.management.model.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 角色分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleQueryDTO extends PageQuery {

    @Serial
    private static final long serialVersionUID = 4429658109977549543L;
    /** 关键词（角色名/角色编码） */
    private String keyword;

    /** 状态（0-禁用，1-正常） */
    private Integer status;
}
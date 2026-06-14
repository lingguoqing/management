package com.management.model.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryDTO extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 5301812929806257439L;

    /** 关键词（用户名/昵称） */
    private String keyword;

    /** 状态（0-禁用，1-正常） */
    private Integer status;

    /** 部门ID */
    private Long deptId;

}
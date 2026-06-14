package com.management.model.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 字典类型分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictTypeQueryDTO extends PageQuery {

    @Serial
    private static final long serialVersionUID = 3497887113277068973L;
    /** 关键词（类型名/类型编码） */
    private String keyword;

    /** 状态（0-禁用，1-正常） */
    private Integer status;
}
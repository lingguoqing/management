package com.management.model.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 字典数据分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictDataQueryDTO extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 5140931538051342363L;

    /** 关键词（标签名/值） */
    private String keyword;

    /** 字典类型编码 */
    private String dictType;

    private Integer status;

}
package com.management.model.dto.query;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页请求基类
 */
@Data
public class PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = -3817916086764893433L;
    /** 当前页码（默认1） */
    private Integer page = 1;

    /** 每页条数（默认10） */
    private Integer pageSize = 10;
}
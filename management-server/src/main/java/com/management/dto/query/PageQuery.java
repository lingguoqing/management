package com.management.dto.query;

import lombok.Data;

/**
 * 分页请求基类
 */
@Data
public class PageQuery {

    /** 当前页码（默认1） */
    private Integer page = 1;

    /** 每页条数（默认10） */
    private Integer pageSize = 10;
}
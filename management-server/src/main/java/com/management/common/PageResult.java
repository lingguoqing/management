package com.management.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页查询统一响应格式
 *
 * @param <T> 分页数据类型
 */
@Data
public class PageResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -5154767920072830938L;
    /** 当前页码 */
    private int page;

    /** 每页条数 */
    private int pageSize;

    /** 总记录数 */
    private int total;

    /** 数据列表 */
    private List<T> records;

    public static <T> PageResult<T> of(int page, int pageSize, int total, List<T> records) {
        PageResult<T> r = new PageResult<>();
        r.page = page;
        r.pageSize = pageSize;
        r.total = total;
        r.records = records;
        return r;
    }
}

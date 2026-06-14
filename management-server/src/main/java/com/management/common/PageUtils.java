package com.management.common;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 分页结果转换工具类
 */
public class PageUtils {

    /**
     * 将 MyBatis-Plus IPage 转换为统一分页响应格式
     */
    public static <T> PageResult<T> convert(IPage<?> page) {
        return PageResult.of(
                (int) page.getCurrent(),
                (int) page.getSize(),
                (int) page.getTotal(),
                (java.util.List<T>) page.getRecords()
        );
    }
}
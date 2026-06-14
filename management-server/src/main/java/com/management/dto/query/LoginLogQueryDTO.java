package com.management.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录日志分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginLogQueryDTO extends PageQuery {

    /** 关键词（用户名/IP地址） */
    private String keyword;

    /** 登录状态（0-失败，1-成功） */
    private Integer status;

    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;
}
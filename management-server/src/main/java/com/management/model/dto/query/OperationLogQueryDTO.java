package com.management.model.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 操作日志分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OperationLogQueryDTO extends PageQuery {

    @Serial
    private static final long serialVersionUID = -7286202973651426309L;
    /** 关键词（操作人/操作内容） */
    private String keyword;

    /** 操作模块 */
    private String module;

    /** 状态（0-失败，1-成功） */
    private Integer status;

    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;
}
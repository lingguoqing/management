package com.management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 查询参数 DTO
 */
@Data
public class QueryDTO {

    /** 当前页码（默认1） */
    private Integer page = 1;

    /** 每页条数（默认10） */
    private Integer pageSize = 10;

    /** 搜索关键字 */
    private String keyword;

    /** 状态筛选 */
    private Integer status;

    /** 部门ID筛选 */
    private Long deptId;

    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;
}

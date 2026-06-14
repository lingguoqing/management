package com.management.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志实体 —— 记录用户增删改操作
 */
@Data
@TableName("sys_operation_log")
public class SysOperationLog implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 操作用户ID */
    private Long userId;

    /** 操作用户名 */
    private String username;

    /** 操作模块 */
    private String module;

    /** 操作类型（如新增、修改、删除） */
    private String operation;

    /** 请求方法（全限定名） */
    private String method;

    /** 请求URL */
    private String requestUrl;

    /** 请求方式（GET/POST/PUT/DELETE） */
    private String requestMethod;

    /** 请求参数（JSON） */
    private String requestParam;

    /** 响应结果（JSON） */
    private String responseResult;

    /** 耗时（毫秒） */
    private Long costTime;

    /** 操作IP */
    private String ip;

    /** 操作地点 */
    private String location;

    /** 操作状态：0-失败 1-成功 */
    private Integer status;

    /** 错误信息 */
    private String errorMsg;

    /** 创建时间 */
    private LocalDateTime createTime;
}

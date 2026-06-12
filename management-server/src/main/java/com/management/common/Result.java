package com.management.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一 API 响应格式
 *
 * @param <T> 响应数据类型
 */
@Data
public class Result<T> implements Serializable {

    /** 状态码：200-成功，其他-失败 */
    private int code;

    /** 提示信息 */
    private String message;

    /** 响应数据 */
    private T data;

    /** 时间戳 */
    private long timestamp;

    private Result() {
        this.timestamp = System.currentTimeMillis();
    }

    /** 成功（无数据） */
    public static <T> Result<T> ok() {
        Result<T> r = new Result<>();
        r.code = 200;
        r.message = "操作成功";
        return r;
    }

    /** 成功（带数据） */
    public static <T> Result<T> ok(T data) {
        Result<T> r = ok();
        r.data = data;
        return r;
    }

    /** 成功（自定义消息+数据） */
    public static <T> Result<T> ok(String message, T data) {
        Result<T> r = ok(data);
        r.message = message;
        return r;
    }

    /** 失败 */
    public static <T> Result<T> fail(int code, String message) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = message;
        return r;
    }

    /** 失败（默认500） */
    public static <T> Result<T> fail(String message) {
        return fail(500, message);
    }

    /** 未授权 */
    public static <T> Result<T> unauthorized() {
        return fail(401, "未登录或登录已过期");
    }

    /** 无权限 */
    public static <T> Result<T> forbidden() {
        return fail(403, "无权限访问");
    }
}

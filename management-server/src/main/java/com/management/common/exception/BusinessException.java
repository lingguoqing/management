package com.management.common.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * 业务异常类
 * 抛出此异常后将由 GlobalExceptionHandler 统一处理
 */
@Getter
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6429942457323659220L;
    /** 错误码 */
    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}

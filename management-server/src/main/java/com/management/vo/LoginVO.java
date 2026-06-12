package com.management.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 登录响应 VO
 * 只返回 token，用户信息通过 /auth/user-info 接口获取
 */
@Data
@Builder
public class LoginVO {

    /** Sa-Token 令牌 */
    private String token;
}
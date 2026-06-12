package com.management.dto;

import lombok.Data;

/**
 * 修改密码 DTO
 */
@Data
public class PasswordDTO {

    private String oldPassword;

    private String newPassword;
}

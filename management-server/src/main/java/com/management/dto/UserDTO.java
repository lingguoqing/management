package com.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户查询 / 新增 / 修改 DTO
 */
@Data
public class UserDTO {

    /** 修改时必填 */
    private Long id;

    @NotBlank(message = "用户名不能为空", groups = {Add.class})
    @Size(min = 2, max = 50, message = "用户名长度为2-50个字符")
    private String username;

    /** 新增时必填，修改时可不填 */
    private String password;

    private String nickname;

    private String email;

    private String phone;

    private Integer gender;

    private Long deptId;

    private Integer status;

    private String remark;

    /** 角色ID列表 */
    private Long[] roleIds;

    /** 新增分组 */
    public interface Add {}
}

package com.management.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 角色实体 —— RBAC 核心，通过 sys_role_permission 关联权限
 * data_scope 控制角色能查看的数据范围
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -6055139577949382885L;
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 角色名称 */
    private String roleName;

    /** 角色编码（唯一，如 admin、editor） */
    private String roleCode;

    /** 角色描述 */
    private String roleDesc;

    /**
     * 数据权限范围：
     * 1-全部数据权限
     * 2-本部门及子部门
     * 3-仅本部门
     * 4-仅本人
     */
    private Integer dataScope;

    /** 排序号 */
    private Integer sortOrder;

    /** 状态：0-停用 1-正常 */
    private Integer status;
}

package com.management.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 部门实体 —— 组织架构树节点
 * parent_id = 0 表示根部门
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
public class SysDept extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -2320337760079905899L;
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 父部门ID（0=顶级） */
    private Long parentId;

    /** 部门名称 */
    private String deptName;

    /** 部门编码（唯一） */
    private String deptCode;

    /** 排序号 */
    private Integer sortOrder;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 状态：0-停用 1-正常 */
    private Integer status;
}

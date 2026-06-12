package com.management.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典类型实体 —— 字典分组
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_type")
public class SysDictType extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 字典名称（如 用户性别） */
    private String dictName;

    /** 字典类型编码（唯一，如 sys_user_gender） */
    private String dictType;

    /** 状态：0-停用 1-正常 */
    private Integer status;

    /** 备注 */
    private String remark;
}

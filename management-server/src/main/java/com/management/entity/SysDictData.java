package com.management.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据实体 —— 字典键值对
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
public class SysDictData extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 字典类型编码（关联 sys_dict_type.dict_type） */
    private String dictType;

    /** 字典标签（显示值） */
    private String dictLabel;

    /** 字典值（存储值） */
    private String dictValue;

    /** 样式类名 */
    private String cssClass;

    /** 表格回显样式 */
    private String listClass;

    /** 是否默认：0-否 1-是 */
    private Integer isDefault;

    /** 排序号 */
    private Integer sortOrder;

    /** 状态：0-停用 1-正常 */
    private Integer status;

    /** 备注 */
    private String remark;
}

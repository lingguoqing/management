package com.management.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类 —— 抽取通用字段
 * 子类通过 @TableName 指定表名
 */
@Data
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -3121076589323006787L;
    /** 逻辑删除：0-未删除 1-已删除 */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted = 0;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

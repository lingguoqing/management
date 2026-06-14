package com.management.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字典数据 DTO
 */
@Data
public class DictDataDTO {

    private Long id;

    @NotBlank(message = "字典类型编码不能为空")
    private String dictType;

    @NotBlank(message = "字典标签不能为空")
    private String dictLabel;

    @NotBlank(message = "字典值不能为空")
    private String dictValue;

    private String cssClass;

    private String listClass;

    private Integer isDefault;

    private Integer sortOrder;

    private Integer status;

    private String remark;
}

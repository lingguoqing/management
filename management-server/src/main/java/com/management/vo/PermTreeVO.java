package com.management.vo;

import lombok.Data;

import java.util.List;

/**
 * 权限树 VO —— 用于角色分配权限时的树形选择
 */
@Data
public class PermTreeVO {

    private Long id;

    private Long parentId;

    /** 权限名称 */
    private String permName;

    /** 权限标识 */
    private String permCode;

    /** 类型 */
    private Integer permType;

    /** 子节点 */
    private List<PermTreeVO> children;
}

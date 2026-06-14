package com.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.model.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;

/**
 * 角色-权限关联 Mapper
 */
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    /** 删除角色的所有权限关联 */
    int deleteByRoleId(@Param("roleId") Long roleId);
}
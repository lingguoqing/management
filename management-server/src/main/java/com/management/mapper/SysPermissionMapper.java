package com.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.model.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限 / 菜单 Mapper
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 查询角色的所有权限ID
     */
    List<Long> selectPermIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询所有状态正常的权限，按排序号升序
     */
    List<SysPermission> selectAllEnabled();
}
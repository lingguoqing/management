package com.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.entity.SysPermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限 / 菜单 Mapper
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 查询角色的所有权限ID
     */
    @Select("SELECT perm_id FROM sys_role_permission WHERE role_id = #{roleId}")
    List<Long> selectPermIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询所有状态正常的权限，按排序号升序
     */
    @Select("SELECT * FROM sys_permission WHERE status = 1 AND deleted = 0 ORDER BY sort_order ASC")
    List<SysPermission> selectAllEnabled();
}

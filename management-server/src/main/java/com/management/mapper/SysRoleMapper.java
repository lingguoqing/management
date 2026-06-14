package com.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色 Mapper
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 查询用户的所有角色
     */
    @Select("SELECT r.* FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.status = 1 AND r.deleted = 0")
    List<SysRole> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据角色编码查询角色
     */
    @Select("SELECT * FROM sys_role WHERE role_code = #{roleCode} AND deleted = 0 LIMIT 1")
    SysRole selectByRoleCode(@Param("roleCode") String roleCode);
}

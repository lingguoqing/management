package com.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.model.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户-角色关联 Mapper
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /** 删除用户的所有角色关联 */
    int deleteByUserId(@Param("userId") Long userId);

    /** 根据角色ID查询所有用户ID */
    List<Long> selectUserIdsByRoleId(@Param("roleId") Long roleId);
}
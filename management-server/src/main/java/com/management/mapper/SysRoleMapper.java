package com.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.model.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色 Mapper
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 查询用户的所有角色
     */
    List<SysRole> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据角色编码查询角色
     */
    SysRole selectByRoleCode(@Param("roleCode") String roleCode);
}
package com.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.model.entity.SysUser;
import com.management.model.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询用户的所有角色编码
     */
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的所有角色ID
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的全部权限标识（含菜单+按钮）
     */
    List<String> selectPermsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的菜单权限（仅目录+菜单，不含按钮）
     * 递归查询：包含直接分配的菜单及其所有父节点，确保能构建完整的菜单树
     */
    List<SysPermission> selectMenusByUserId(@Param("userId") Long userId);
}
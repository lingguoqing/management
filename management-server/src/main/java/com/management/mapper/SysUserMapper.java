package com.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户 Mapper
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询用户的所有角色编码
     */
    @Select("SELECT r.role_code FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.status = 1 AND r.deleted = 0")
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的全部权限标识（含菜单+按钮）
     */
    @Select("SELECT DISTINCT p.permission FROM sys_permission p " +
            "INNER JOIN sys_role_permission rp ON p.id = rp.perm_id " +
            "INNER JOIN sys_user_role ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND p.status = 1 AND p.deleted = 0 AND p.permission IS NOT NULL AND p.permission != ''")
    List<String> selectPermsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的菜单权限（仅目录+菜单，不含按钮）
     * 递归查询：包含直接分配的菜单及其所有父节点，确保能构建完整的菜单树
     */
    @Select("WITH RECURSIVE menu_tree AS (" +
            "  SELECT p.* FROM sys_permission p " +
            "  INNER JOIN sys_role_permission rp ON p.id = rp.perm_id " +
            "  INNER JOIN sys_user_role ur ON rp.role_id = ur.role_id " +
            "  WHERE ur.user_id = #{userId} AND p.status = 1 AND p.deleted = 0 " +
            "  AND p.type IN (0, 1) AND p.visible = 1 " +
            "  UNION " +
            "  SELECT p.* FROM sys_permission p " +
            "  INNER JOIN menu_tree mt ON p.id = mt.parent_id " +
            "  WHERE p.status = 1 AND p.deleted = 0 AND p.type IN (0, 1) AND p.visible = 1" +
            ") " +
            "SELECT DISTINCT * FROM menu_tree ORDER BY sort ASC")
    List<com.management.entity.SysPermission> selectMenusByUserId(@Param("userId") Long userId);
}

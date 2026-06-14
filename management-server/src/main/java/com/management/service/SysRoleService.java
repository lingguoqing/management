package com.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.model.dto.RoleDTO;
import com.management.model.dto.query.RoleQueryDTO;
import com.management.model.entity.SysRole;

import java.util.List;

/**
 * 角色 Service 接口
 */
public interface SysRoleService extends IService<SysRole> {

    /** 分页查询角色列表 */
    IPage<SysRole> pageRoles(RoleQueryDTO query);

    /** 新增角色（含权限关联） */
    void createRole(RoleDTO dto);

    /** 修改角色（含权限关联） */
    void updateRole(RoleDTO dto);

    /** 分配角色权限 */
    void assignPermissions(Long roleId, Long[] permIds);

    /** 删除角色 */
    void deleteRole(Long id);

    /** 查询所有角色（下拉列表用） */
    List<SysRole> listAll();

    /** 查询角色的权限ID列表 */
    List<Long> getRolePermIds(Long roleId);
}

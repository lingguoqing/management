package com.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.model.dto.PermissionDTO;
import com.management.model.entity.SysPermission;
import com.management.model.vo.MenuVO;
import com.management.model.vo.PermTreeVO;

import java.util.List;

/**
 * 权限 Service 接口
 */
public interface SysPermissionService extends IService<SysPermission> {

    /** 获取权限树（用于角色分配权限） */
    List<PermTreeVO> getPermTree();

    /** 根据用户ID获取菜单树 */
    List<MenuVO> getMenuTreeByUserId(Long userId);

    /** 获取单个权限详情 */
    PermissionDTO getPermissionById(Long id);

    /** 新增权限/菜单 */
    void createPermission(PermissionDTO dto);

    /** 修改权限/菜单 */
    void updatePermission(PermissionDTO dto);

    /** 删除权限/菜单（级联删除子节点） */
    void deletePermission(Long id);
}

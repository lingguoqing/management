package com.management.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.common.exception.BusinessException;
import com.management.dto.PermissionDTO;
import com.management.entity.SysPermission;
import com.management.mapper.SysPermissionMapper;
import com.management.mapper.SysRolePermissionMapper;
import com.management.mapper.SysUserMapper;
import com.management.service.SysPermissionService;
import com.management.vo.MenuVO;
import com.management.vo.PermTreeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限 / 菜单 Service 实现
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    private final SysRolePermissionMapper rolePermissionMapper;
    private final SysUserMapper userMapper;

    @Override
    public List<PermTreeVO> getPermTree() {
        List<SysPermission> all = baseMapper.selectAllEnabled();
        return buildPermTree(all, 0L);
    }

    @Override
    public List<MenuVO> getMenuTreeByUserId(Long userId) {
        // 超级管理员拥有所有菜单权限
        List<SysPermission> menus = userMapper.selectMenusByUserId(userId);
        return buildMenuTree(menus, 0L);
    }

    @Override
    public PermissionDTO getPermissionById(Long id) {
        SysPermission perm = baseMapper.selectById(id);
        if (perm == null) {
            throw new BusinessException("权限不存在");
        }
        return BeanUtil.copyProperties(perm, PermissionDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPermission(PermissionDTO dto) {
        // 按钮类型必须有权限标识
        if (dto.getPermType() == 3 && (dto.getPermCode() == null || dto.getPermCode().isBlank())) {
            throw new BusinessException("按钮权限必须填写权限标识");
        }
        // 菜单类型必须有路由路径
        if (dto.getPermType() == 2 && (dto.getPath() == null || dto.getPath().isBlank())) {
            throw new BusinessException("菜单类型必须填写路由路径");
        }

        SysPermission perm = BeanUtil.copyProperties(dto, SysPermission.class);
        baseMapper.insert(perm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(PermissionDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("权限ID不能为空");
        }
        SysPermission perm = baseMapper.selectById(dto.getId());
        if (perm == null) {
            throw new BusinessException("权限不存在");
        }

        // 不能将父节点设为自己
        if (dto.getParentId() != null && dto.getParentId().equals(dto.getId())) {
            throw new BusinessException("父节点不能是自己");
        }

        BeanUtil.copyProperties(dto, perm, CopyOptions.create().setIgnoreNullValue(true).setIgnoreProperties("id"));
        baseMapper.updateById(perm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long id) {
        SysPermission perm = baseMapper.selectById(id);
        if (perm == null) {
            throw new BusinessException("权限不存在");
        }

        // 级联删除子节点
        List<Long> idsToDelete = new ArrayList<>();
        idsToDelete.add(id);
        collectChildIds(id, idsToDelete);

        baseMapper.deleteBatchIds(idsToDelete);

        // 清除角色-权限关联
        for (Long permId : idsToDelete) {
            rolePermissionMapper.delete(
                    new LambdaQueryWrapper<com.management.entity.SysRolePermission>()
                            .eq(com.management.entity.SysRolePermission::getPermId, permId));
        }
    }

    // ---- 私有辅助方法 ----

    /** 构建权限树 */
    private List<PermTreeVO> buildPermTree(List<SysPermission> all, Long parentId) {
        return all.stream()
                .filter(p -> p.getParentId().equals(parentId))
                .map(p -> {
                    PermTreeVO vo = BeanUtil.copyProperties(p, PermTreeVO.class);
                    vo.setChildren(buildPermTree(all, p.getId()));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    /** 构建菜单树 */
    private List<MenuVO> buildMenuTree(List<SysPermission> all, Long parentId) {
        return all.stream()
                .filter(p -> p.getParentId().equals(parentId))
                .map(p -> {
                    MenuVO vo = BeanUtil.copyProperties(p, MenuVO.class);
                    vo.setChildren(buildMenuTree(all, p.getId()));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    /** 递归收集所有子节点ID */
    private void collectChildIds(Long parentId, List<Long> ids) {
        List<SysPermission> children = baseMapper.selectList(
                new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getParentId, parentId));
        for (SysPermission child : children) {
            ids.add(child.getId());
            collectChildIds(child.getId(), ids);
        }
    }
}

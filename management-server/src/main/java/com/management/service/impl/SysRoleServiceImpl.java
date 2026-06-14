package com.management.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.common.exception.BusinessException;
import com.management.dto.RoleDTO;
import com.management.dto.query.RoleQueryDTO;
import com.management.entity.SysRole;
import com.management.entity.SysRolePermission;
import com.management.mapper.SysRoleMapper;
import com.management.mapper.SysRolePermissionMapper;
import com.management.mapper.SysUserRoleMapper;
import com.management.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色 Service 实现
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRolePermissionMapper rolePermissionMapper;
    private final SysUserRoleMapper userRoleMapper;

    @Override
    public IPage<SysRole> pageRoles(RoleQueryDTO query) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.like(SysRole::getRoleName, query.getKeyword())
                    .or().like(SysRole::getRoleCode, query.getKeyword());
        }
        if (query.getStatus() != null) {
            wrapper.eq(SysRole::getStatus, query.getStatus());
        }
        wrapper.orderByAsc(SysRole::getSortOrder);
        return baseMapper.selectPage(new Page<>(query.getPage(), query.getPageSize()), wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(RoleDTO dto) {
        // 检查角色编码是否已存在
        SysRole exist = baseMapper.selectOne(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, dto.getRoleCode()));
        if (exist != null) {
            throw new BusinessException("角色编码已存在");
        }

        SysRole role = BeanUtil.copyProperties(dto, SysRole.class);
        baseMapper.insert(role);

        // 保存角色-权限关联
        saveRolePermissions(role.getId(), dto.getPermIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("角色ID不能为空");
        }

        SysRole role = baseMapper.selectById(dto.getId());
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 检查角色编码是否被其他角色占用
        if (!role.getRoleCode().equals(dto.getRoleCode())) {
            SysRole exist = baseMapper.selectOne(
                    new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, dto.getRoleCode()));
            if (exist != null) {
                throw new BusinessException("角色编码已被占用");
            }
        }

        BeanUtil.copyProperties(dto, role, CopyOptions.create().setIgnoreNullValue(true).setIgnoreProperties("id"));
        baseMapper.updateById(role);

        // 重新保存角色-权限关联
        rolePermissionMapper.deleteByRoleId(dto.getId());
        saveRolePermissions(dto.getId(), dto.getPermIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(Long roleId, Long[] permIds) {
        rolePermissionMapper.deleteByRoleId(roleId);
        saveRolePermissions(roleId, permIds);
        // 踢出该角色下的所有在线用户，强制重新登录以刷新权限
        List<Long> userIds = userRoleMapper.selectUserIdsByRoleId(roleId);
        for (Long userId : userIds) {
            try {
                StpUtil.kickout(userId);
            } catch (Exception ignored) {
                // 用户不在线，跳过
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        SysRole role = baseMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        baseMapper.deleteById(id);
        rolePermissionMapper.deleteByRoleId(id);
    }

    @Override
    public List<SysRole> listAll() {
        return baseMapper.selectList(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getStatus, 1).orderByAsc(SysRole::getSortOrder));
    }

    @Override
    public List<Long> getRolePermIds(Long roleId) {
        return rolePermissionMapper.selectList(
                        new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, roleId))
                .stream()
                .map(SysRolePermission::getPermId)
                .collect(Collectors.toList());
    }

    /**
     * 保存角色-权限关联
     */
    private void saveRolePermissions(Long roleId, Long[] permIds) {
        if (permIds == null || permIds.length == 0) {
            return;
        }
        List<SysRolePermission> list = Arrays.stream(permIds)
                .map(permId -> {
                    SysRolePermission rp = new SysRolePermission();
                    rp.setRoleId(roleId);
                    rp.setPermId(permId);
                    return rp;
                })
                .collect(Collectors.toList());
        rolePermissionMapper.insert(list);
    }
}

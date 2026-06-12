package com.management.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.common.exception.BusinessException;
import com.management.dto.PasswordDTO;
import com.management.dto.QueryDTO;
import com.management.dto.UserDTO;
import com.management.entity.*;
import com.management.mapper.*;
import com.management.service.SysPermissionService;
import com.management.service.SysUserService;
import com.management.vo.MenuVO;
import com.management.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户 Service 实现
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserRoleMapper userRoleMapper;
    private final SysPermissionService permissionService;
    private final SysDeptMapper deptMapper;

    @Override
    public IPage<SysUser> pageUsers(QueryDTO query) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        // 关键字搜索：用户名 / 昵称 / 手机号
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w.like(SysUser::getUsername, query.getKeyword())
                    .or().like(SysUser::getNickname, query.getKeyword())
                    .or().like(SysUser::getPhone, query.getKeyword()));
        }
        if (query.getStatus() != null) {
            wrapper.eq(SysUser::getStatus, query.getStatus());
        }
        if (query.getDeptId() != null) {
            wrapper.eq(SysUser::getDeptId, query.getDeptId());
        }
        wrapper.orderByAsc(SysUser::getCreateTime);
        return baseMapper.selectPage(new Page<>(query.getPage(), query.getPageSize()), wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserDTO dto) {
        // 检查用户名是否已存在
        SysUser exist = baseMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername()));
        if (exist != null) {
            throw new BusinessException("用户名已存在");
        }

        SysUser user = BeanUtil.copyProperties(dto, SysUser.class);
        // 密码 BCrypt 加密
        user.setPassword(BCrypt.hashpw(dto.getPassword()));
        baseMapper.insert(user);

        // 保存用户-角色关联
        saveUserRoles(user.getId(), dto.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }

        SysUser user = baseMapper.selectById(dto.getId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查用户名是否被其他用户占用
        if (!user.getUsername().equals(dto.getUsername())) {
            SysUser exist = baseMapper.selectOne(
                    new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername()));
            if (exist != null) {
                throw new BusinessException("用户名已被占用");
            }
        }

        BeanUtil.copyProperties(dto, user, CopyOptions.create().setIgnoreNullValue(true).setIgnoreProperties("password", "id"));
        // 如果填写了新密码则更新
        if (StrUtil.isNotBlank(dto.getPassword())) {
            user.setPassword(BCrypt.hashpw(dto.getPassword()));
        }
        baseMapper.updateById(user);

        // 重新保存用户-角色关联
        userRoleMapper.deleteByUserId(dto.getId());
        saveUserRoles(dto.getId(), dto.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        SysUser user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 逻辑删除
        baseMapper.deleteById(id);
        // 清除角色关联
        userRoleMapper.deleteByUserId(id);
    }

    @Override
    public UserInfoVO getCurrentUserInfo(Long userId) {
        SysUser user = baseMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserInfoVO vo = BeanUtil.copyProperties(user, UserInfoVO.class);

        // 查询部门名称
        if (user.getDeptId() != null) {
            SysDept dept = deptMapper.selectById(user.getDeptId());
            if (dept != null) {
                vo.setDeptName(dept.getDeptName());
            }
        }

        // 获取角色编码列表
        List<String> roleCodes = baseMapper.selectRoleCodesByUserId(userId);
        vo.setRoles(roleCodes);

        // 获取权限标识集合
        List<String> perms = baseMapper.selectPermsByUserId(userId);
        vo.setPermissions(new HashSet<>(perms));

        // 获取菜单树
        List<MenuVO> menus = permissionService.getMenuTreeByUserId(userId);
        vo.setMenus(menus);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long userId, PasswordDTO dto) {
        SysUser user = baseMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 校验旧密码
        if (!BCrypt.checkpw(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }

        // 更新密码
        user.setPassword(BCrypt.hashpw(dto.getNewPassword()));
        baseMapper.updateById(user);
    }

    /**
     * 保存用户-角色关联（先删后插）
     */
    private void saveUserRoles(Long userId, Long[] roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            return;
        }
        List<SysUserRole> list = Arrays.stream(roleIds)
                .map(roleId -> {
                    SysUserRole ur = new SysUserRole();
                    ur.setUserId(userId);
                    ur.setRoleId(roleId);
                    return ur;
                })
                .collect(Collectors.toList());
        userRoleMapper.insert(list);
    }
}

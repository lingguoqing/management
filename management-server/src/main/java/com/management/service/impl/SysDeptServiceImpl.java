package com.management.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.common.exception.BusinessException;
import com.management.model.dto.DeptDTO;
import com.management.model.entity.SysDept;
import com.management.mapper.SysDeptMapper;
import com.management.service.SysDeptService;
import com.management.model.vo.DeptTreeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门 Service 实现
 */
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public List<DeptTreeVO> getDeptTree() {
        List<SysDept> all = baseMapper.selectList(
                new LambdaQueryWrapper<SysDept>().orderByAsc(SysDept::getSortOrder));
        return buildTree(all, 0L);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDept(DeptDTO dto) {
        SysDept dept = BeanUtil.copyProperties(dto, SysDept.class);
        baseMapper.insert(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(DeptDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("部门ID不能为空");
        }
        SysDept dept = baseMapper.selectById(dto.getId());
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }
        if (dto.getParentId() != null && dto.getParentId().equals(dto.getId())) {
            throw new BusinessException("父部门不能是自己");
        }
        BeanUtil.copyProperties(dto, dept, CopyOptions.create().setIgnoreNullValue(true).setIgnoreProperties("id"));
        baseMapper.updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(Long id) {
        SysDept dept = baseMapper.selectById(id);
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }
        // 检查是否有子部门
        Long childCount = baseMapper.selectCount(
                new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, id));
        if (childCount > 0) {
            throw new BusinessException("该部门下存在子部门，无法删除");
        }
        // TODO: 检查是否有用户关联该部门
        baseMapper.deleteById(id);
    }

    private List<DeptTreeVO> buildTree(List<SysDept> all, Long parentId) {
        return all.stream()
                .filter(d -> d.getParentId().equals(parentId))
                .map(d -> {
                    DeptTreeVO vo = BeanUtil.copyProperties(d, DeptTreeVO.class);
                    vo.setChildren(buildTree(all, d.getId()));
                    return vo;
                })
                .collect(Collectors.toList());
    }
}

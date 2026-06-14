package com.management.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.common.exception.BusinessException;
import com.management.model.dto.DictTypeDTO;
import com.management.model.dto.query.DictTypeQueryDTO;
import com.management.model.entity.SysDictType;
import com.management.mapper.SysDictTypeMapper;
import com.management.service.SysDictTypeService;
import org.springframework.stereotype.Service;

/**
 * 字典类型 Service 实现
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    @Override
    public IPage<SysDictType> pageDictTypes(DictTypeQueryDTO query) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.like(SysDictType::getDictName, query.getKeyword())
                    .or().like(SysDictType::getDictType, query.getKeyword());
        }
        if (query.getStatus() != null) {
            wrapper.eq(SysDictType::getStatus, query.getStatus());
        }
        wrapper.orderByAsc(SysDictType::getCreateTime);
        return baseMapper.selectPage(new Page<>(query.getPage(), query.getPageSize()), wrapper);
    }

    @Override
    public void createDictType(DictTypeDTO dto) {
        SysDictType exist = baseMapper.selectOne(
                new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getDictType, dto.getDictType()));
        if (exist != null) {
            throw new BusinessException("字典类型编码已存在");
        }
        SysDictType entity = BeanUtil.copyProperties(dto, SysDictType.class);
        baseMapper.insert(entity);
    }

    @Override
    public void updateDictType(DictTypeDTO dto) {
        SysDictType entity = baseMapper.selectById(dto.getId());
        if (entity == null) {
            throw new BusinessException("字典类型不存在");
        }
        // 检查编码是否被其他记录占用
        if (!entity.getDictType().equals(dto.getDictType())) {
            SysDictType exist = baseMapper.selectOne(
                    new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getDictType, dto.getDictType()));
            if (exist != null) {
                throw new BusinessException("字典类型编码已被占用");
            }
        }
        BeanUtil.copyProperties(dto, entity, CopyOptions.create().setIgnoreNullValue(true).setIgnoreProperties("id"));
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteDictType(Long id) {
        baseMapper.deleteById(id);
    }
}

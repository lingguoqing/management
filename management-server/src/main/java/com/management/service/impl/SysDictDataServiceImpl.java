package com.management.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.common.exception.BusinessException;
import com.management.dto.DictDataDTO;
import com.management.dto.query.DictDataQueryDTO;
import com.management.entity.SysDictData;
import com.management.mapper.SysDictDataMapper;
import com.management.service.SysDictDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典数据 Service 实现
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService {

    @Override
    public IPage<SysDictData> pageDictData(DictDataQueryDTO query) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.like(SysDictData::getDictLabel, query.getKeyword());
        }
        if (query.getStatus() != null) {
            wrapper.eq(SysDictData::getStatus, query.getStatus());
        }
        wrapper.orderByAsc(SysDictData::getSortOrder);
        return baseMapper.selectPage(new Page<>(query.getPage(), query.getPageSize()), wrapper);
    }

    @Override
    public List<SysDictData> listByDictType(String dictType) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<SysDictData>()
                        .eq(SysDictData::getDictType, dictType)
                        .eq(SysDictData::getStatus, 1)
                        .orderByAsc(SysDictData::getSortOrder));
    }

    @Override
    public void createDictData(DictDataDTO dto) {
        SysDictData entity = BeanUtil.copyProperties(dto, SysDictData.class);
        baseMapper.insert(entity);
    }

    @Override
    public void updateDictData(DictDataDTO dto) {
        SysDictData entity = baseMapper.selectById(dto.getId());
        if (entity == null) {
            throw new BusinessException("字典数据不存在");
        }
        BeanUtil.copyProperties(dto, entity, CopyOptions.create().setIgnoreNullValue(true).setIgnoreProperties("id"));
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteDictData(Long id) {
        baseMapper.deleteById(id);
    }
}

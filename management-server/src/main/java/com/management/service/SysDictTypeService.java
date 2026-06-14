package com.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.model.dto.DictTypeDTO;
import com.management.model.dto.query.DictTypeQueryDTO;
import com.management.model.entity.SysDictType;

/**
 * 字典类型 Service 接口
 */
public interface SysDictTypeService extends IService<SysDictType> {

    /** 分页查询 */
    IPage<SysDictType> pageDictTypes(DictTypeQueryDTO query);

    /** 新增 */
    void createDictType(DictTypeDTO dto);

    /** 修改 */
    void updateDictType(DictTypeDTO dto);

    /** 删除 */
    void deleteDictType(Long id);
}

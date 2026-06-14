package com.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.model.dto.DictDataDTO;
import com.management.model.dto.query.DictDataQueryDTO;
import com.management.model.entity.SysDictData;

import java.util.List;

/**
 * 字典数据 Service 接口
 */
public interface SysDictDataService extends IService<SysDictData> {

    /** 分页查询 */
    IPage<SysDictData> pageDictData(DictDataQueryDTO query);

    /** 按字典类型查询 */
    List<SysDictData> listByDictType(String dictType);

    /** 新增 */
    void createDictData(DictDataDTO dto);

    /** 修改 */
    void updateDictData(DictDataDTO dto);

    /** 删除 */
    void deleteDictData(Long id);
}

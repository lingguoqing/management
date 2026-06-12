package com.management.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.common.PageResult;
import com.management.common.Result;
import com.management.common.annotation.OperLog;
import com.management.dto.DictDataDTO;
import com.management.dto.DictTypeDTO;
import com.management.dto.QueryDTO;
import com.management.entity.SysDictData;
import com.management.entity.SysDictType;
import com.management.service.SysDictDataService;
import com.management.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理控制器
 */
@Tag(name = "字典管理")
@RestController
@RequestMapping("/api/dict")
@RequiredArgsConstructor
public class DictController {

    private final SysDictTypeService dictTypeService;
    private final SysDictDataService dictDataService;

    // ---- 字典类型 ----

    @Operation(summary = "分页查询字典类型")
    @GetMapping("/type/page")
    @SaCheckPermission("sys:dict:list")
    public Result<PageResult<SysDictType>> pageTypes(QueryDTO query) {
        IPage<SysDictType> page = dictTypeService.pageDictTypes(query);
        return Result.ok(PageResult.of((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal(), page.getRecords()));
    }

    @Operation(summary = "新增字典类型")
    @OperLog(module = "字典管理", operation = "新增类型")
    @PostMapping("/type")
    @SaCheckPermission("sys:dict:list")
    public Result<Void> createType(@Valid @RequestBody DictTypeDTO dto) {
        dictTypeService.createDictType(dto);
        return Result.ok();
    }

    @Operation(summary = "修改字典类型")
    @OperLog(module = "字典管理", operation = "修改类型")
    @PutMapping("/type")
    @SaCheckPermission("sys:dict:list")
    public Result<Void> updateType(@Valid @RequestBody DictTypeDTO dto) {
        dictTypeService.updateDictType(dto);
        return Result.ok();
    }

    @Operation(summary = "删除字典类型")
    @OperLog(module = "字典管理", operation = "删除类型")
    @DeleteMapping("/type/{id}")
    @SaCheckPermission("sys:dict:list")
    public Result<Void> deleteType(@PathVariable Long id) {
        dictTypeService.deleteDictType(id);
        return Result.ok();
    }

    // ---- 字典数据 ----

    @Operation(summary = "分页查询字典数据")
    @GetMapping("/data/page")
    @SaCheckPermission("sys:dict:list")
    public Result<PageResult<SysDictData>> pageData(QueryDTO query) {
        IPage<SysDictData> page = dictDataService.pageDictData(query);
        return Result.ok(PageResult.of((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal(), page.getRecords()));
    }

    @Operation(summary = "根据字典类型查询字典数据")
    @GetMapping("/data/{dictType}")
    public Result<List<SysDictData>> listByType(@PathVariable String dictType) {
        return Result.ok(dictDataService.listByDictType(dictType));
    }

    @Operation(summary = "新增字典数据")
    @OperLog(module = "字典管理", operation = "新增数据")
    @PostMapping("/data")
    @SaCheckPermission("sys:dict:list")
    public Result<Void> createData(@Valid @RequestBody DictDataDTO dto) {
        dictDataService.createDictData(dto);
        return Result.ok();
    }

    @Operation(summary = "修改字典数据")
    @OperLog(module = "字典管理", operation = "修改数据")
    @PutMapping("/data")
    @SaCheckPermission("sys:dict:list")
    public Result<Void> updateData(@Valid @RequestBody DictDataDTO dto) {
        dictDataService.updateDictData(dto);
        return Result.ok();
    }

    @Operation(summary = "删除字典数据")
    @OperLog(module = "字典管理", operation = "删除数据")
    @DeleteMapping("/data/{id}")
    @SaCheckPermission("sys:dict:list")
    public Result<Void> deleteData(@PathVariable Long id) {
        dictDataService.deleteDictData(id);
        return Result.ok();
    }
}

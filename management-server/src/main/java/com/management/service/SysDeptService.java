package com.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.model.dto.DeptDTO;
import com.management.model.entity.SysDept;
import com.management.model.vo.DeptTreeVO;

import java.util.List;

/**
 * 部门 Service 接口
 */
public interface SysDeptService extends IService<SysDept> {

    /** 获取部门树 */
    List<DeptTreeVO> getDeptTree();

    /** 新增部门 */
    void createDept(DeptDTO dto);

    /** 修改部门 */
    void updateDept(DeptDTO dto);

    /** 删除部门（含子部门） */
    void deleteDept(Long id);
}

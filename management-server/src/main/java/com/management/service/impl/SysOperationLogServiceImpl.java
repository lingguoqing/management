package com.management.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.model.dto.query.OperationLogQueryDTO;
import com.management.model.entity.SysOperationLog;
import com.management.mapper.SysOperationLogMapper;
import com.management.service.SysOperationLogService;
import org.springframework.stereotype.Service;

/**
 * 操作日志 Service 实现
 */
@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements SysOperationLogService {

    @Override
    public IPage<SysOperationLog> pageLogs(OperationLogQueryDTO query) {
        LambdaQueryWrapper<SysOperationLog> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.like(SysOperationLog::getUsername, query.getKeyword())
                    .or().like(SysOperationLog::getOperation, query.getKeyword())
                    .or().like(SysOperationLog::getModule, query.getKeyword());
        }
        if (query.getStatus() != null) {
            wrapper.eq(SysOperationLog::getStatus, query.getStatus());
        }
        if (StrUtil.isNotBlank(query.getStartTime())) {
            wrapper.ge(SysOperationLog::getCreateTime, query.getStartTime());
        }
        if (StrUtil.isNotBlank(query.getEndTime())) {
            wrapper.le(SysOperationLog::getCreateTime, query.getEndTime());
        }
        wrapper.orderByDesc(SysOperationLog::getCreateTime);
        return baseMapper.selectPage(new Page<>(query.getPage(), query.getPageSize()), wrapper);
    }

    @Override
    public void clearLogs() {
        baseMapper.delete(new LambdaQueryWrapper<>());
    }
}

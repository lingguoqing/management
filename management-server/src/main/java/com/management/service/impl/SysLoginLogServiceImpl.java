package com.management.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.model.dto.query.LoginLogQueryDTO;
import com.management.model.entity.SysLoginLog;
import com.management.mapper.SysLoginLogMapper;
import com.management.service.SysLoginLogService;
import org.springframework.stereotype.Service;

/**
 * 登录日志 Service 实现
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Override
    public IPage<SysLoginLog> pageLogs(LoginLogQueryDTO query) {
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.like(SysLoginLog::getUsername, query.getKeyword());
        }
        if (query.getStatus() != null) {
            wrapper.eq(SysLoginLog::getStatus, query.getStatus());
        }
        if (StrUtil.isNotBlank(query.getStartTime())) {
            wrapper.ge(SysLoginLog::getLoginTime, query.getStartTime());
        }
        if (StrUtil.isNotBlank(query.getEndTime())) {
            wrapper.le(SysLoginLog::getLoginTime, query.getEndTime());
        }
        wrapper.orderByDesc(SysLoginLog::getLoginTime);
        return baseMapper.selectPage(new Page<>(query.getPage(), query.getPageSize()), wrapper);
    }

    @Override
    public void clearLogs() {
        baseMapper.delete(new LambdaQueryWrapper<>());
    }
}

package com.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.dto.query.LoginLogQueryDTO;
import com.management.entity.SysLoginLog;

/**
 * 登录日志 Service 接口
 */
public interface SysLoginLogService extends IService<SysLoginLog> {

    /** 分页查询 */
    IPage<SysLoginLog> pageLogs(LoginLogQueryDTO query);

    /** 清空日志 */
    void clearLogs();
}

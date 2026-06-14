package com.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.dto.query.OperationLogQueryDTO;
import com.management.entity.SysOperationLog;

/**
 * 操作日志 Service 接口
 */
public interface SysOperationLogService extends IService<SysOperationLog> {

    /** 分页查询 */
    IPage<SysOperationLog> pageLogs(OperationLogQueryDTO query);

    /** 清空日志 */
    void clearLogs();
}

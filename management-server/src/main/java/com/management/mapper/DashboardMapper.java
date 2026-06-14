package com.management.mapper;

/**
 * 仪表盘统计 Mapper
 */
public interface DashboardMapper {

    /** 统计用户总数 */
    long countUsers();

    /** 统计角色总数 */
    long countRoles();

    /** 统计菜单权限总数 */
    long countPermissions();

    /** 统计今日操作日志数量 */
    long countTodayOperations();
}
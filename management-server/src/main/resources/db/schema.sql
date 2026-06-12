-- =====================================================
-- RBAC 管理平台 数据库初始化脚本
-- 数据库: MySQL 8.0+
-- 字符集: utf8mb4
-- =====================================================

CREATE DATABASE IF NOT EXISTS `management_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `management_db`;

-- =====================================================
-- 1. 部门表 sys_dept
-- 说明: 组织架构树，支持无限层级
-- =====================================================
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
    `id`            BIGINT          NOT NULL    COMMENT '部门ID',
    `parent_id`     BIGINT          DEFAULT 0   COMMENT '父部门ID，0表示根部门',
    `dept_name`     VARCHAR(100)    NOT NULL    COMMENT '部门名称',
    `dept_code`     VARCHAR(50)     NOT NULL    COMMENT '部门编码（唯一）',
    `sort_order`    INT             DEFAULT 0   COMMENT '排序号（升序）',
    `leader`        VARCHAR(50)     DEFAULT NULL COMMENT '负责人',
    `phone`         VARCHAR(20)     DEFAULT NULL COMMENT '联系电话',
    `email`         VARCHAR(100)    DEFAULT NULL COMMENT '邮箱',
    `status`        TINYINT         DEFAULT 1   COMMENT '状态：0-停用 1-正常',
    `deleted`       TINYINT         DEFAULT 0   COMMENT '逻辑删除：0-未删除 1-已删除',
    `create_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dept_code` (`dept_code`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='部门表';

-- =====================================================
-- 2. 用户表 sys_user
-- 说明: 平台用户，关联部门，支持多角色
-- =====================================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id`            BIGINT          NOT NULL    COMMENT '用户ID',
    `username`      VARCHAR(50)     NOT NULL    COMMENT '用户名（登录账号，唯一）',
    `password`      VARCHAR(200)    NOT NULL    COMMENT '密码（BCrypt加密）',
    `nickname`      VARCHAR(50)     DEFAULT NULL COMMENT '昵称/姓名',
    `avatar`        VARCHAR(500)    DEFAULT NULL COMMENT '头像URL',
    `email`         VARCHAR(100)    DEFAULT NULL COMMENT '邮箱',
    `phone`         VARCHAR(20)     DEFAULT NULL COMMENT '手机号',
    `gender`        TINYINT         DEFAULT 0   COMMENT '性别：0-未知 1-男 2-女',
    `dept_id`       BIGINT          DEFAULT NULL COMMENT '所属部门ID',
    `status`        TINYINT         DEFAULT 1   COMMENT '状态：0-停用 1-正常',
    `remark`        VARCHAR(500)    DEFAULT NULL COMMENT '备注',
    `last_login_time` DATETIME      DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50)     DEFAULT NULL COMMENT '最后登录IP',
    `deleted`       TINYINT         DEFAULT 0   COMMENT '逻辑删除：0-未删除 1-已删除',
    `create_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- =====================================================
-- 3. 角色表 sys_role
-- 说明: RBAC 核心——角色，关联权限
-- =====================================================
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id`            BIGINT          NOT NULL    COMMENT '角色ID',
    `role_name`     VARCHAR(50)     NOT NULL    COMMENT '角色名称',
    `role_code`     VARCHAR(50)     NOT NULL    COMMENT '角色编码（唯一，如 admin、editor）',
    `role_desc`     VARCHAR(200)    DEFAULT NULL COMMENT '角色描述',
    `data_scope`    TINYINT         DEFAULT 1   COMMENT '数据权限范围：1-全部 2-本部门及子部门 3-本部门 4-本人',
    `sort_order`    INT             DEFAULT 0   COMMENT '排序号（升序）',
    `status`        TINYINT         DEFAULT 1   COMMENT '状态：0-停用 1-正常',
    `deleted`       TINYINT         DEFAULT 0   COMMENT '逻辑删除：0-未删除 1-已删除',
    `create_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

-- =====================================================
-- 4. 权限/菜单表 sys_permission
-- 说明: 菜单 + 按钮权限合一，通过 type 区分；使用 parent_id 构建树形结构
--       type: 1-目录  2-菜单  3-按钮
-- =====================================================
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
    `id`            BIGINT          NOT NULL    COMMENT '权限ID',
    `parent_id`     BIGINT          DEFAULT 0   COMMENT '父权限ID，0表示顶级',
    `perm_name`     VARCHAR(50)     NOT NULL    COMMENT '权限名称',
    `perm_code`     VARCHAR(100)    DEFAULT NULL COMMENT '权限标识（如 sys:user:add），按钮权限必填',
    `perm_type`     TINYINT         NOT NULL    COMMENT '类型：1-目录 2-菜单 3-按钮',
    `path`          VARCHAR(200)    DEFAULT NULL COMMENT '路由路径（菜单类型必填）',
    `component`     VARCHAR(200)    DEFAULT NULL COMMENT '组件路径（菜单类型必填）',
    `icon`          VARCHAR(50)     DEFAULT NULL COMMENT '菜单图标',
    `sort_order`    INT             DEFAULT 0   COMMENT '排序号（升序）',
    `visible`       TINYINT         DEFAULT 1   COMMENT '是否可见：0-隐藏 1-显示',
    `keep_alive`    TINYINT         DEFAULT 0   COMMENT '是否缓存：0-否 1-是',
    `status`        TINYINT         DEFAULT 1   COMMENT '状态：0-停用 1-正常',
    `deleted`       TINYINT         DEFAULT 0   COMMENT '逻辑删除：0-未删除 1-已删除',
    `create_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_perm_type` (`perm_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限/菜单表';

-- =====================================================
-- 5. 用户-角色关联表 sys_user_role
-- 说明: 多对多关联——一个用户可有多个角色
-- =====================================================
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
    `id`            BIGINT          NOT NULL    COMMENT '主键ID',
    `user_id`       BIGINT          NOT NULL    COMMENT '用户ID',
    `role_id`       BIGINT          NOT NULL    COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户-角色关联表';

-- =====================================================
-- 6. 角色-权限关联表 sys_role_permission
-- 说明: 多对多关联——一个角色可有多个权限
-- =====================================================
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
    `id`            BIGINT          NOT NULL    COMMENT '主键ID',
    `role_id`       BIGINT          NOT NULL    COMMENT '角色ID',
    `perm_id`       BIGINT          NOT NULL    COMMENT '权限ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_perm` (`role_id`, `perm_id`),
    KEY `idx_perm_id` (`perm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色-权限关联表';

-- =====================================================
-- 7. 字典类型表 sys_dict_type
-- 说明: 系统字典分类，如"性别"、"状态"等
-- =====================================================
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
    `id`            BIGINT          NOT NULL    COMMENT '字典类型ID',
    `dict_name`     VARCHAR(100)    NOT NULL    COMMENT '字典名称（如"用户性别"）',
    `dict_type`     VARCHAR(100)    NOT NULL    COMMENT '字典类型编码（如 sys_user_gender，唯一）',
    `status`        TINYINT         DEFAULT 1   COMMENT '状态：0-停用 1-正常',
    `remark`        VARCHAR(500)    DEFAULT NULL COMMENT '备注',
    `deleted`       TINYINT         DEFAULT 0   COMMENT '逻辑删除：0-未删除 1-已删除',
    `create_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典类型表';

-- =====================================================
-- 8. 字典数据表 sys_dict_data
-- 说明: 字典的具体键值对，关联字典类型
-- =====================================================
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
    `id`            BIGINT          NOT NULL    COMMENT '字典数据ID',
    `dict_type`     VARCHAR(100)    NOT NULL    COMMENT '字典类型编码（关联 sys_dict_type.dict_type）',
    `dict_label`    VARCHAR(100)    NOT NULL    COMMENT '字典标签（显示值）',
    `dict_value`    VARCHAR(100)    NOT NULL    COMMENT '字典值（存储值）',
    `css_class`     VARCHAR(100)    DEFAULT NULL COMMENT '样式类名',
    `list_class`    VARCHAR(100)    DEFAULT NULL COMMENT '表格回显样式',
    `is_default`    TINYINT         DEFAULT 0   COMMENT '是否默认：0-否 1-是',
    `sort_order`    INT             DEFAULT 0   COMMENT '排序号（升序）',
    `status`        TINYINT         DEFAULT 1   COMMENT '状态：0-停用 1-正常',
    `remark`        VARCHAR(500)    DEFAULT NULL COMMENT '备注',
    `deleted`       TINYINT         DEFAULT 0   COMMENT '逻辑删除：0-未删除 1-已删除',
    `create_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典数据表';

-- =====================================================
-- 9. 操作日志表 sys_operation_log
-- 说明: 记录用户的操作行为（增删改）
-- =====================================================
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
    `id`            BIGINT          NOT NULL    COMMENT '日志ID',
    `user_id`       BIGINT          DEFAULT NULL COMMENT '操作用户ID',
    `username`      VARCHAR(50)     DEFAULT NULL COMMENT '操作用户名',
    `module`        VARCHAR(100)    DEFAULT NULL COMMENT '操作模块',
    `operation`     VARCHAR(100)    DEFAULT NULL COMMENT '操作类型（如：新增、修改、删除）',
    `method`        VARCHAR(200)    DEFAULT NULL COMMENT '请求方法（全限定名）',
    `request_url`   VARCHAR(500)    DEFAULT NULL COMMENT '请求URL',
    `request_method` VARCHAR(10)    DEFAULT NULL COMMENT '请求方式（GET/POST/PUT/DELETE）',
    `request_param` TEXT            DEFAULT NULL COMMENT '请求参数（JSON）',
    `response_result` TEXT          DEFAULT NULL COMMENT '响应结果（JSON，截取前2000字符）',
    `cost_time`     BIGINT          DEFAULT 0   COMMENT '耗时（毫秒）',
    `ip`            VARCHAR(50)     DEFAULT NULL COMMENT '操作IP',
    `location`      VARCHAR(100)    DEFAULT NULL COMMENT '操作地点',
    `status`        TINYINT         DEFAULT 1   COMMENT '操作状态：0-失败 1-成功',
    `error_msg`     TEXT            DEFAULT NULL COMMENT '错误信息',
    `create_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='操作日志表';

-- =====================================================
-- 10. 登录日志表 sys_login_log
-- 说明: 记录用户登录历史
-- =====================================================
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
    `id`            BIGINT          NOT NULL    COMMENT '日志ID',
    `user_id`       BIGINT          DEFAULT NULL COMMENT '用户ID',
    `username`      VARCHAR(50)     DEFAULT NULL COMMENT '登录用户名',
    `login_time`    DATETIME        DEFAULT NULL COMMENT '登录时间',
    `ip`            VARCHAR(50)     DEFAULT NULL COMMENT '登录IP',
    `location`      VARCHAR(100)    DEFAULT NULL COMMENT '登录地点',
    `browser`       VARCHAR(100)    DEFAULT NULL COMMENT '浏览器类型',
    `os`            VARCHAR(100)    DEFAULT NULL COMMENT '操作系统',
    `status`        TINYINT         DEFAULT 1   COMMENT '登录状态：0-失败 1-成功',
    `msg`           VARCHAR(200)    DEFAULT NULL COMMENT '提示信息',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='登录日志表';

-- =====================================================
-- 初始化数据：默认管理员账号 admin / admin123
-- 密码为 BCrypt 加密后的 "admin123"
-- =====================================================

-- 默认部门
INSERT INTO `sys_dept` (`id`, `parent_id`, `dept_name`, `dept_code`, `sort_order`, `leader`, `status`) VALUES
(1, 0, '总公司', 'ROOT', 0, '管理员', 1),
(2, 1, '技术部', 'TECH', 1, '张三', 1),
(3, 1, '市场部', 'MARKET', 2, '李四', 1);

-- 默认管理员用户 (admin/admin123)
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `email`, `dept_id`, `status`) VALUES
(1, 'admin', '$2a$10$NZ5o7r2E.4YxR6qPqHqCHe0QHJrX0J0jSmzZ0K3uLCB3qG5KzC2qy', '超级管理员', 'admin@example.com', 2, 1);

-- 默认角色
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `role_desc`, `data_scope`, `sort_order`, `status`) VALUES
(1, '超级管理员', 'admin', '拥有系统全部权限', 1, 0, 1),
(2, '普通用户', 'user', '普通用户，仅可查看', 4, 1, 1);

-- 默认权限（菜单 + 按钮）
INSERT INTO `sys_permission` (`id`, `parent_id`, `perm_name`, `perm_code`, `perm_type`, `path`, `component`, `icon`, `sort_order`) VALUES
-- 一级目录
(1,  0,  '系统管理', NULL,          1, '/system',    NULL,           'SettingOutlined', 0),
(2,  0,  '系统监控', NULL,          1, '/monitor',   NULL,           'MonitorOutlined', 1),
-- 二级菜单（系统管理）
(10, 1,  '用户管理', 'sys:user:list',  2, '/system/user',  'system/user/index',    'UserOutlined',     0),
(11, 1,  '角色管理', 'sys:role:list',  2, '/system/role',  'system/role/index',    'TeamOutlined',     1),
(12, 1,  '菜单管理', 'sys:perm:list',  2, '/system/permission', 'system/permission/index', 'MenuOutlined', 2),
(13, 1,  '部门管理', 'sys:dept:list',  2, '/system/dept',  'system/dept/index',    'ApartmentOutlined', 3),
(14, 1,  '字典管理', 'sys:dict:list',  2, '/system/dict',  'system/dict/index',    'BookOutlined',     4),
-- 二级菜单（系统监控）
(20, 2,  '操作日志', 'sys:log:operation', 2, '/monitor/operation-log', 'monitor/log/operation', 'FileTextOutlined', 0),
(21, 2,  '登录日志', 'sys:log:login',     2, '/monitor/login-log',    'monitor/log/login',    'SolutionOutlined', 1),
-- 按钮权限（用户管理）
(100, 10, '新增用户', 'sys:user:add',    3, NULL, NULL, NULL, 0),
(101, 10, '修改用户', 'sys:user:edit',   3, NULL, NULL, NULL, 1),
(102, 10, '删除用户', 'sys:user:delete', 3, NULL, NULL, NULL, 2),
(103, 10, '导出用户', 'sys:user:export', 3, NULL, NULL, NULL, 3),
-- 按钮权限（角色管理）
(110, 11, '新增角色', 'sys:role:add',    3, NULL, NULL, NULL, 0),
(111, 11, '修改角色', 'sys:role:edit',   3, NULL, NULL, NULL, 1),
(112, 11, '删除角色', 'sys:role:delete', 3, NULL, NULL, NULL, 2),
(113, 11, '分配权限', 'sys:role:assign-perm', 3, NULL, NULL, NULL, 3),
-- 按钮权限（菜单管理）
(120, 12, '新增菜单', 'sys:perm:add',    3, NULL, NULL, NULL, 0),
(121, 12, '修改菜单', 'sys:perm:edit',   3, NULL, NULL, NULL, 1),
(122, 12, '删除菜单', 'sys:perm:delete', 3, NULL, NULL, NULL, 2);

-- 管理员角色-权限关联（拥有全部权限）
INSERT INTO `sys_role_permission` (`id`, `role_id`, `perm_id`) VALUES
(1, 1, 1), (2, 1, 2), (3, 1, 10), (4, 1, 11), (5, 1, 12), (6, 1, 13), (7, 1, 14),
(8, 1, 20), (9, 1, 21),
(10, 1, 100), (11, 1, 101), (12, 1, 102), (13, 1, 103),
(14, 1, 110), (15, 1, 111), (16, 1, 112), (17, 1, 113),
(18, 1, 120), (19, 1, 121), (20, 1, 122);

-- 用户-角色关联（admin = 超级管理员）
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES (1, 1, 1);

-- 默认字典类型
INSERT INTO `sys_dict_type` (`id`, `dict_name`, `dict_type`, `status`) VALUES
(1, '用户性别', 'sys_user_gender', 1),
(2, '系统状态', 'sys_status', 1),
(3, '权限类型', 'sys_perm_type', 1),
(4, '数据权限范围', 'sys_data_scope', 1);

-- 默认字典数据
INSERT INTO `sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort_order`, `is_default`) VALUES
(1,  'sys_user_gender', '未知', '0', 0, 1),
(2,  'sys_user_gender', '男',   '1', 1, 0),
(3,  'sys_user_gender', '女',   '2', 2, 0),
(4,  'sys_status',      '正常', '1', 0, 1),
(5,  'sys_status',      '停用', '0', 1, 0),
(6,  'sys_perm_type',   '目录', '1', 0, 0),
(7,  'sys_perm_type',   '菜单', '2', 1, 0),
(8,  'sys_perm_type',   '按钮', '3', 2, 0),
(9,  'sys_data_scope',  '全部数据权限',     '1', 0, 0),
(10, 'sys_data_scope',  '本部门及子部门',   '2', 1, 0),
(11, 'sys_data_scope',  '仅本部门',         '3', 2, 0),
(12, 'sys_data_scope',  '仅本人',           '4', 3, 0);

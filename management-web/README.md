# RBAC 管理平台

基于 **Spring Boot 3.5 + Vue 3 + Ant Design Vue** 的 RBAC 权限管理系统，前后端分离架构。

## 技术栈

### 后端
- **Java 21** + **Spring Boot 3.5.14**
- **MyBatis-Plus** 3.5.7 — ORM 框架
- **Sa-Token** 1.38.0 — 权限认证框架（RBAC）
- **Knife4j** 4.5.0 — API 文档
- **Hutool** 5.8.28 — 工具集
- **MySQL** 8.0+ — 数据库
- **Lombok** — 代码简化

### 前端
- **Vue 3** + **TypeScript**
- **Ant Design Vue 4** — UI 组件库
- **Pinia** — 状态管理（含持久化）
- **Vue Router 4** — 路由
- **Vue I18n** — 国际化（中/英文）
- **Axios** — HTTP 请求
- **Vite** — 构建工具

## 项目结构

```
management/
├── management-server/       # 后端 Spring Boot 工程
│   ├── src/main/java/com/management/
│   │   ├── common/          # 公共类（统一响应、异常）
│   │   ├── config/          # 配置（SaToken、MyBatisPlus、Knife4j、CORS）
│   │   ├── controller/      # 控制器层
│   │   ├── dto/             # 数据传输对象
│   │   ├── entity/          # 实体类（映射数据库表）
│   │   ├── mapper/          # MyBatis-Plus Mapper
│   │   ├── service/         # 业务逻辑层
│   │   └── vo/              # 视图对象
│   └── src/main/resources/
│       ├── application.yml  # 应用配置
│       └── db/schema.sql    # 数据库初始化脚本
│
├── management-web/          # 前端 Vue 3 工程
│   ├── src/
│   │   ├── api/             # API 接口层
│   │   ├── assets/          # 静态资源 + 全局样式
│   │   ├── components/      # 公共组件
│   │   ├── locales/         # 国际化资源
│   │   ├── router/          # 路由配置
│   │   ├── stores/          # Pinia 状态
│   │   ├── utils/           # 工具函数
│   │   └── views/           # 页面组件
│   └── vite.config.ts       # Vite 配置
```

## 数据库表设计

| 表名 | 说明 | 核心字段 |
|------|------|---------|
| `sys_user` | 用户表 | username, password(BCrypt), dept_id, status |
| `sys_role` | 角色表 | role_name, role_code(唯一), data_scope |
| `sys_permission` | 权限/菜单表 | perm_name, perm_code, perm_type(1目录2菜单3按钮), parent_id, path, component |
| `sys_user_role` | 用户-角色关联 | user_id, role_id (联合唯一) |
| `sys_role_permission` | 角色-权限关联 | role_id, perm_id (联合唯一) |
| `sys_dept` | 部门表 | dept_name, dept_code, parent_id (树形结构) |
| `sys_dict_type` | 字典类型表 | dict_name, dict_type(唯一) |
| `sys_dict_data` | 字典数据表 | dict_type, dict_label, dict_value |
| `sys_operation_log` | 操作日志表 | user_id, module, operation, request_url, ip |
| `sys_login_log` | 登录日志表 | user_id, ip, browser, os, status |

### 权限模型说明
- **RBAC 模型**: 用户 → 角色 → 权限（多对多关联）
- **菜单结构**: 通过 `parent_id` 构建无限层级树形菜单
- **权限标识**: 按钮权限使用 `perm_code`（如 `sys:user:add`），通过 `@SaCheckPermission` 注解校验
- **数据权限**: 角色通过 `data_scope` 控制数据可见范围（全部/本部门及子部门/本部门/本人）

## 快速开始

### 1. 数据库初始化
```bash
# 创建数据库并执行初始化脚本
mysql -u root -p < management-server/src/main/resources/db/schema.sql
```

### 2. 启动后端
```bash
cd management-server
mvn spring-boot:run
# 访问 API 文档: http://localhost:8080/doc.html
```

### 3. 启动前端
```bash
cd management-web
npm install
npm run dev
# 访问: http://localhost:5173
```

### 4. 登录
- 默认管理员账号: `admin` / `admin123`

## API 接口概览

| 模块 | 路径 | 说明 |
|------|------|------|
| 认证 | `/api/auth/**` | 登录、登出、用户信息、修改密码 |
| 用户管理 | `/api/user/**` | 用户 CRUD + 分页查询 |
| 角色管理 | `/api/role/**` | 角色 CRUD + 权限分配 |
| 菜单管理 | `/api/permission/**` | 菜单树 CRUD |
| 部门管理 | `/api/dept/**` | 部门树 CRUD |
| 字典管理 | `/api/dict/**` | 字典类型 + 字典数据 |
| 日志管理 | `/api/log/**` | 操作日志 + 登录日志 |

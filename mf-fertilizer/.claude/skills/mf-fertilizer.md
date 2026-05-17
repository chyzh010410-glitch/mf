---
name: mf-fertilizer
description: 开发苗丰施肥管控平台（苗木/树木施肥管理后台系统），后端基于 Java 17 + SpringBoot + MyBatis-Plus + MySQL + Redis，严格遵循指定技术栈、架构规范、业务需求、代码标准。
allowed-tools: Bash
argument-hint: "[项目开发指令]"
effort: high
---

## 一、固定技术栈（100% 严格遵守，不可替换）
1. **开发语言**：Java 17（LTS 长期支持版，强制使用）
2. **核心框架**：SpringBoot 3.x（适配 Java17）、Spring MVC
3. **持久层框架**：MyBatis-Plus
4. **数据库**：MySQL 8.0+
5. **构建工具**：Maven
6. **缓存中间件**：Redis
7. **登录认证**：JWT
8. **接口文档**：Knife4j / Swagger
9. **工具库**：Lombok、Hutool、Spring Validation

## 二、必须集成的核心依赖
1. Lombok：简化实体类代码
2. Spring Validation：接口入参校验
3. Knife4j：自动生成可视化接口文档
4. Hutool：通用工具类（日期、计算、字符串处理）
5. JWT：无状态登录令牌生成与校验

## 三、Redis 业务用途（必须实现）
1. 缓存登录用户 JWT 令牌，校验登录状态
2. 缓存常用树种、肥料基础数据列表
3. 缓存施肥智能推荐结果，避免重复计算
4. 支持统计数据缓存优化

## 四、项目标准包结构（Java17 项目）
com.mf.fertilizer
├── controller      # 接口层（接收前端请求）
├── service         # 业务接口
├── serviceImpl     # 业务实现（核心逻辑）
├── mapper          # MyBatis-Plus 数据访问
├── entity          # 数据库实体类
├── dto             # 前端入参封装
├── vo              # 前端出参封装
├── config          # 配置类（Redis、MP、跨域、Swagger）
├── exception       # 全局异常、自定义异常
├── util            # 工具类
├── constant        # 系统常量

## 五、数据库设计规范（强制）
1. 核心表：用户表、树木表、肥料表、施肥记录表、施肥推荐规则表
2. 所有表必须包含：主键(雪花ID)、create_time、update_time、deleted(逻辑删除)
3. 使用 MyBatis-Plus 逻辑删除，不物理删除数据
4. 不使用数据库外键，关联由代码控制
5. 时间字段统一使用 `LocalDateTime` (Java17 标准)
6. 给查询高频字段添加索引

## 六、Java17 + 全局开发规范
1. **统一接口返回格式**：所有接口返回标准结构
2. **全局异常处理器**：统一拦截异常，返回友好提示
3. **跨域配置**：支持前后端分离
4. **自动填充时间**：MyBatis-Plus 自动赋值创建/更新时间
5. **分页查询**：所有列表接口默认分页
6. **VO/DTO 隔离**：禁止直接返回 entity 给前端
7. **接口参数校验**：使用 Spring Validation 注解
8. **代码风格**：遵循 Java17 规范，支持 var 关键字，代码简洁清晰

## 七、登录与权限（Java17 + JWT）
1. JWT + 拦截器实现无状态登录
2. 角色：管理员、普通操作员
3. 令牌校验、过期控制
4. 权限分级控制

## 八、核心业务功能
1. 用户管理：登录、权限控制
2. 树木管理：增删改查、分页、筛选
3. 肥料管理：增删改查、分类
4. 施肥记录：新增、历史查询、条件筛选
5. 数据统计：施肥次数、肥料用量统计
6. **智能施肥推荐**：按树种、树龄、季节匹配规则推荐

## 九、Java17 强制要求
1. 项目 JDK 版本：Java 17
2. SpringBoot 版本：3.x 系列
3. 日期 API：必须使用 `java.time` 包（LocalDateTime、LocalDate）
4. 支持 Java17 新特性，代码简洁规范
5. 编译、运行环境全部基于 Java17

## 十、扩展预留（无需开发，预留结构）
1. 文件上传接口
2. 定时任务（施肥提醒）
3. 数据可视化统计接口

## 十一、开发指令
收到指令后，**严格按照以上所有规范**，使用 Java 17 开发苗丰施肥管理后台系统，包括：项目初始化、包结构搭建、配置类、数据库设计、核心业务接口、全局规范。
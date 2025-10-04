# Nacos 代码库文档

## 📋 项目概述

**Nacos** (Dynamic **Na**ming and **Co**nfiguration **S**ervice) 是阿里巴巴开源的动态服务发现、配置管理和服务管理平台。

- **版本**: 3.1.0
- **组织**: 阿里巴巴集团
- **许可**: Apache License 2.0
- **官网**: https://nacos.io
- **Java 版本**: 17 (客户端支持 Java 8)

### 核心功能

1. **服务发现与健康检查** - 服务注册、发现、实时健康检查
2. **动态配置管理** - 集中化配置管理,无需重启应用即可更新配置
3. **动态 DNS 服务** - 支持权重路由、中间层负载均衡
4. **服务和元数据管理** - 服务仪表板、配置管理、Kubernetes DNS、指标统计

---

## 🏗️ 项目架构说明
**Nacos (中间件项目)**:
```
nacos/
├── naming/        # 服务注册与发现
├── config/        # 配置管理
├── client/        # 客户端 SDK
├── core/          # 核心引擎
└── ...           # 其他功能模块
```
→ **按功能模块划分**,每个模块是独立的技术能力,类似微服务架构

### 设计原则

1. **模块化隔离** - 每个模块可独立开发、测试、发布
2. **可插拔** - 用户只依赖需要的模块 (如只用 config,不用 naming)
3. **可扩展** - 通过 plugin 模块支持第三方扩展
4. **职责清晰** - 每个模块只负责一个领域的功能

---

## 📦 模块说明

### 核心业务模块

| 模块 | 说明 | 关键目录 |
|------|------|----------|
| **naming** | 服务注册与发现 | `core/`, `controllers/`, `remote/rpc/handler/` |
| **config** | 配置管理 | `server/` |
| **client** | 客户端 SDK | Nacos 客户端库,供应用集成 |
| **console** | 管理控制台 | Web 管理界面后端 |
| **console-ui** | 前端界面 | React/Vue 前端代码 |

### 基础设施模块

| 模块 | 说明 |
|------|------|
| **api** | 对外 API 定义,包含 gRPC/HTTP 接口 |
| **common** | 公共工具类、常量、帮助类 |
| **core** | 核心功能,如集群管理、通知机制 |
| **persistence** | 数据持久化,支持 MySQL/Derby |
| **consistency** | 一致性协议,基于 Raft 算法 |
| **auth** | 权限认证模块 |
| **plugin** | 插件接口定义 |
| **plugin-default-impl** | 默认插件实现 |

### 扩展模块

| 模块 | 说明 |
|------|------|
| **istio** | Istio 服务网格集成 |
| **k8s-sync** | Kubernetes 同步 |
| **prometheus** | Prometheus 指标监控 |
| **ai** | AI 增强功能 |
| **mcp-registry-adaptor** | MCP 注册表适配器 |

### 构建模块

| 模块 | 说明 |
|------|------|
| **bootstrap** | 启动引导程序 |
| **distribution** | 打包分发配置 |
| **server** | 服务器主程序 |
| **example** | 示例代码 |
| **test** | 集成测试 |

---

## 🗺️ 模块内部结构

每个核心模块内部仍然有分层,只是命名不同:

### Naming 模块示例

```
naming/src/main/java/com/alibaba/nacos/naming/
├── controllers/       # → 相当于 Controller 层
│   └── *Controller.java
├── core/              # → 相当于 Service 层
│   ├── Service.java
│   └── ServiceManager.java
├── consistency/       # → 数据一致性/持久化
├── remote/rpc/handler/  # → gRPC 处理器 (类似 Controller)
├── healthcheck/       # 健康检查
├── push/              # 服务推送
├── model/             # 数据模型
├── pojo/              # POJO 对象
└── NamingApp.java     # 模块启动类
```

### Config 模块示例

```
config/src/main/java/com/alibaba/nacos/config/
└── server/
    ├── controller/    # HTTP 接口
    ├── service/       # 业务逻辑
    ├── model/         # 数据模型
    └── remote/        # 远程通信
```

---

## 🧭 代码导航指南

### 快速入门路径

1. **了解对外能力** → 阅读 `api` 模块
   - 查看 `api/src/main/java/com/alibaba/nacos/api/`
   - 重点关注接口定义和数据模型

2. **理解客户端使用** → 阅读 `client` 模块和 `example` 模块
   - `example/` - 包含使用示例
   - `client/src/main/java/com/alibaba/nacos/client/`

3. **深入核心实现**:
   - **服务注册发现** → `naming/` 模块
     - 入口: `naming/src/main/java/com/alibaba/nacos/naming/core/ServiceManager.java`
     - 处理器: `naming/remote/rpc/handler/InstanceRequestHandler.java`

   - **配置管理** → `config/` 模块
     - 入口: `config/src/main/java/com/alibaba/nacos/config/server/service/`

   - **数据一致性** → `consistency/` 模块
     - Raft 实现,保证分布式一致性

4. **服务器启动** → `bootstrap/` 和 `server/` 模块
   - `bootstrap/src/main/java/com/alibaba/nacos/Nacos.java` - 主启动类

### 关键代码位置

| 功能 | 位置 |
|------|------|
| 服务注册 | `naming/core/ServiceManager.java` |
| 实例管理 | `naming/remote/rpc/handler/InstanceRequestHandler.java` |
| 配置发布 | `config/server/service/ConfigCacheService.java` |
| 配置监听 | `config/server/service/LongPollingService.java` |
| 集群管理 | `core/src/main/java/com/alibaba/nacos/core/cluster/` |
| Raft 一致性 | `consistency/src/main/java/com/alibaba/nacos/consistency/cp/` |
| gRPC 通信 | `core/src/main/java/com/alibaba/nacos/core/remote/grpc/` |

---

## 🔧 技术栈

### 核心依赖

- **Spring Boot**: 3.4.9
- **gRPC**: 1.75.0 - RPC 通信框架
- **JRaft**: 1.3.15 - Raft 一致性算法
- **Protobuf**: 3.25.5 - 序列化协议
- **MySQL**: 8.2.0 / Derby 10.14.2.0 - 数据库
- **Logback**: 1.5.12 - 日志框架

### 构建工具

- **Maven**: 3.2.5+
- **Java**: 17

---

## 🚀 本地开发

### 启动服务器

```bash
# 1. 编译项目
mvn clean package -DskipTests

# 2. 启动 Nacos Server (单机模式)
cd distribution/target/nacos-server-3.1.0/nacos/bin
sh startup.sh -m standalone  # Linux/Mac
startup.cmd -m standalone    # Windows
```

### 访问控制台

- URL: http://localhost:8848/nacos
- 默认账号: nacos / nacos

### 运行测试

```bash
# 单元测试
mvn test

# 配置模块集成测试
mvn test -Pcit-test

# Naming 模块集成测试
mvn test -Pnit-test
```

---

## 📚 阅读建议

### 对于初学者

1. 先看 `README.md` 了解项目整体
2. 运行 `example/` 中的示例代码,体验 Nacos 功能
3. 阅读 `api/` 模块,理解 API 设计
4. 查看 `client/` 模块,了解客户端实现
5. 最后深入 `naming/` 和 `config/` 核心逻辑

### 对于进阶开发者

1. 研究 `consistency/` 模块的 Raft 实现
2. 分析 `core/` 模块的集群管理和 gRPC 通信
3. 查看 `plugin/` 机制,了解扩展点
4. 阅读 `persistence/` 模块的数据持久化策略

### 调试技巧

1. **设置断点位置**:
   - 服务注册: `InstanceRequestHandler.handle()`
   - 配置发布: `ConfigController.publishConfig()`
   - 集群同步: `RaftCore.signalPublish()`

2. **日志配置**: `distribution/conf/nacos-logback.xml`

3. **配置文件**: `distribution/conf/application.properties`

---

## 🌟 与传统项目对比

| 维度 | 传统 MVC 项目 | Nacos 中间件项目 |
|------|---------------|------------------|
| **组织方式** | 按技术分层 (controller/service/dao) | 按功能模块 (naming/config/client) |
| **目标** | 实现业务功能 | 提供技术能力 |
| **复用性** | 项目内复用 | 跨项目复用 (SDK) |
| **部署** | 单一应用 | 服务器 + 客户端 SDK |
| **扩展** | 新增业务模块 | 插件机制 |
| **示例** | 电商、CRM 系统 | Nacos, Redis, Kafka |

---

## 📖 参考资源

- [官方文档](https://nacos.io/docs/latest/)
- [架构与原理电子书](https://nacos.io/docs/ebook/kbyo6n/)
- [GitHub Issues](https://github.com/alibaba/nacos/issues)
- [用户邮件组](mailto:users-nacos@googlegroups.com)
- [开发者邮件组](mailto:dev-nacos@googlegroups.com)

---

## 🤝 贡献指南

查看 [CONTRIBUTING.md](./CONTRIBUTING.md) 了解如何参与贡献。

适合新手的 Issue:
- [good first issue](https://github.com/alibaba/nacos/issues?q=is%3Aopen+is%3Aissue+label%3A%22good+first+issue%22)
- [contribution welcome](https://github.com/alibaba/nacos/issues?q=is%3Aopen+is%3Aissue+label%3A%22contribution+welcome%22)

---

*最后更新: 2025-10-04*
*文档版本: 1.0*

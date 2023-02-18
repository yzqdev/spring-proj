

<h1 align="center"><img align="left" height="100px" src="https://user-images.githubusercontent.com/68722157/138828506-f58b7c57-5e10-4178-8f7d-5d5e12050113.png"> Yi框架</h1>
<h4 align="center">一套与SqlSugar一样爽的.Net6开源框架</h4>
<h2 align="center">集大成者，终究轮子</h2>

[English](README-en.md) | 简体中文

![sdk](https://img.shields.io/badge/sdk-6.0.1-d.svg)![License MIT](https://img.shields.io/badge/license-Apache-blue.svg?style=flat-square)

****
### 简介:
**中文：意框架**（和他的名字一样“简易”，同时接入Java的Ruoyi Vue3.0前端）

模块分化较多，可根据业务自行引用或抛弃，集大成者，大而全乎，也许你能从中学习到一些独特见解

正在持续更进业务模块，已接入ruoyi

**英文：YiFramework**

Yi框架-一套与SqlSugar一样爽的.Net6低代码开源框架。
与Sqlsugar理念一致，以用户体验出发。
架构干净整洁、无业务代码、采用微软风格原生框架封装、CodeFrist+配套自研文件模板代码生成器 开发。
适合.Net6学习、Sqlsugar学习 、项目二次开发。
集大成者，终究轮子

Yi框架最新版本标签：`v1.2.1`，具体版本可以查看标签迭代

（项目与Sqlsugar同步更新，但这作者老杰哥代码天天爆肝到凌晨两点，我们也尽量会跟上他的脚步。更新频繁，所以可watching持续关注。）

————这不仅仅是一个程序，更是一个艺术品，面向艺术的开发！

**分支**：

（本项目由EFCore版本历经3年不断迭代至Sqlsugar版本，现EFcore版本已弃用，目前sqlsugar已带业务功能）

**SqlSugar**:.Net6 DDD领域驱动设计 简单分层微服务架构

- Yi.Framework.Net6：.NetCore 6 意框架 （后端）

- Yi.Vue3.X.RuoYi：Vue3 RuoYi前端框架 （前端后台）

  （你没有听错，已经接入java流行指数最高最火爆的框架之一，与其他框架不同，Yi框架后端为完全重制版，并非为ruoyi java模仿版）
  
- Yi.Vue3.x.Vant：Vue3 移动端前端框架 （前端前台）持续迭代

**SqlSugar-Dev**：为sqlsugar分支的实时开发版本

~~**ec**: EFcore完整电商项目~~

****

### 演示地址：

废话少说直接上地址，**请不要**更改里面的数据

API服务：[yi.ccnetcore.com](http://yi.ccnetcore.com)    管理员账号：cc 、 123456

网关地址：~~[gate.ccnetcore.com/swagger](http://gate.ccnetcore.com/swagger)~~

~~WebFirst开发：所有代码生成器已经配置完成，无需任何操作数据库及任何代码，只需要网页表格上点点点即可~~

代码自动生成：

![代码生成](Readme/%E4%BB%A3%E7%A0%81%E7%94%9F%E6%88%90.gif)

### 支持:

- [x] 完全支持单体应用架构
- [x] 完全支持分布式应用架构
- [x] 完全支持微服务架构
- [ ] 即将支持网格服务架构（我们将在后续版本加入dapr）

****
### 软件架构:

**架构**：后端.NET6(Asp.NetCore 6)、WebFirst代码生成器~~与.NET5(Asp.NetCore 5)、前端Vue（2.0）~~

**关系型数据库**：mysql、sql server、sqlite、oracle(正在兼容中)

**操作系统**：Windows、Linux

**身份验证**：JWT、IdentityServer4

**组件**：SqlSugar、Autofac、Castle、Swagger、Log4Net、Redis、RabbitMq、ES、Quartz.net、~~T4~~

**分布式**：CAP、Lock

**微服务**：Consul、Ocelot、IdentityService、Apollo、Docker、Jenkins、Nginx、K8s、ELK、Polly

**封装**：Json处理模块，滑动验证码模块，base64图片处理模块，异常捕捉模块、邮件处理模块、linq封装模块、随机数模块、统一接口模块、基于策略的jwt验证、过滤器、数据库连接、跨域、初始化种子数据、Base32、Console输出、日期处理、文件传输、html筛选、http请求、ip过滤、md5加密、Rsa加密、序列化、雪花算法、字符串处理、编码处理、地址处理、xml处理、心跳检查。。。

****
<h3>业务支持模块</h3>

RABC权限管理系统（正在更新）
（大部分ruoyi功能，采用ruoyi前端）
- 用户管理
- 角色管理
- 菜单管理
- 部门管理
- 岗位管理
- 字典管理
- 参数管理
- 用户在线
- 操作日志
- 登录日志
- 等等

ERP进销存系统（正在更新）
- 供货商管理
- 等等

BBS论坛系统（持续迭代）
- 文章管理
- 评论管理
- 等等

SHOP电商系统（持续迭代）
- SPU管理
- SKU管理
- 商品规格
- 商品分类
- 等等


![输入图片说明](Readme/%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86.png)

![输入图片说明](Readme/%E8%8F%9C%E5%8D%95%E7%AE%A1%E7%90%86.png)

### 框架支持模块:

大致如图：

![image](https://user-images.githubusercontent.com/68722157/142923071-2fa524eb-e833-4143-a926-51566e56e889.png)
![image](https://user-images.githubusercontent.com/68722157/142923150-ebe1b538-c3fc-42dd-bea8-83e10e0f819a.png)
![image](https://user-images.githubusercontent.com/68722157/142923529-e4fbb2f6-def1-4702-b9da-5adbd22f0a2f.png)

(删除线代表已实现功能还未迁移过来)
- [x] 支持大致`DDD领域驱动设计`进行分层，支持微服务扩展
- [x] 支持采用`异步`开发awit/async
- [x] 支持数据库主从`读写分离`
- [x] 支持功能替换，无需改动代码，只需配置`json文件`进行装配即可
- [x] 支持`Aop封装`，FilterAop、IocAop、LogAop、SqlAop
- [x] 支持`Log4Net日志`记录，自动生成至bin目录下的logs文件夹
- [x] 支持`DbSeed数据库种子数据`接入
- [x] 支持主流`数据库随意切换`，Mysql/Sqlite/Sqlserver/Oracle
- [x] 支持上海杰哥官方`SqlSugar ORM`封装
- [x] 支持新版`SwaggerWebAPI`，jwt身份认证接入
- [x] 支持`Cors`跨域
- [x] 支持`AutoFac`自动映射依赖注入
- [x] 支持`consul`服务器注册与发现
- [x] 支持`健康检查`
- [x] 支持`RabbitMQ`消息队列
- [x] 支持`Redis`多级缓存
- [x] 支持`MemoryCache`多级缓存 
- [x] 支持`Ocelot`网关，路由、服务聚合、服务发现、认证、鉴权、限流、熔断、缓存、Header头传递
- [x] 支持`Apollo`全局配置中心;
- [x] 支持`docker`镜像制作
- [x] 支持`Quartz.net`任务调度，实现任意接口被调度
- [x] 支持`ThumbnailSharp`缩略图封装
- [x] 支持`ELK`，log4net+kafka+es+logstach+kibana
- [x] 支持`IdentityService4`授权中心
- [x] 支持`Es`分词查询
- [x] 支持多级`缓存`
- [x] 支持`CAP`分布式事务，mysql+rabbitMq
- [x] 支持`Docker+k8s`部署
- [x] 支持`Jenkins+CI/CD`
- [x] 支持`AutoMapper`模块映射
- [x] 支持`微信支付`模块
- [x] 支持`单表多租户`常用功能
- [x] 支持`逻辑删除`常用功能
- [x] 支持`操作日志`常用功能
- [x] 支持`自动分表`
- [x] 支持`数据权限`
- [x] 支持`CodeFrist`快速构建数据库
- [x] 支持自研文件版`代码生成器`快速构建通用代码
- [x] 支持完整`Dto`模式
- [x] 支持 太多了忘了


****
### 目录结构:

![image](https://s1.ax1x.com/2022/04/09/LCTleH.png)
![image](https://s1.ax1x.com/2022/04/24/L4qlSs.png)
![image](https://s1.ax1x.com/2022/04/24/L4q1ln.png)

我们大致依照DDD领域驱动设计分层

分层如此清晰！什么？还感觉太复杂了？用户只需关注Api、Service其他都是轮子啊！

- BackGround：后台进程（通常使用消息队列进行消费任务）
- Test：测试（单元测试）
- Domain：领域层（Dto、服务接口层、模型层、仓储层、服务层）
- Infrastructure：基础实例层(通用工具层、核心层、定时任务Job、国际化、Web扩展层)
- Module：模块层（其他模块，可按需进行引入，例如微信支付、代码生成）
- MicroServiceInstance：服务层（微服务）

****
### 安装教程:

我们将在之后更新教程手册！

后端
1.  下载全部源码，默认使用sqlite数据库，已经生成
2.  直接点击sln文件运行即可，没有任何其他依赖

前端
1.  下载全部源码，npm i 安装依赖
2.  使用npm run dev进行运行


****
### 使用说明:

~~1.  导入使用仓库中的WebFirst数据库~~
~~2.  使用WebFirst添加实体、同步实体、修改模板生成路径并生成方案~~

 没了，恭喜你已经成功完成了项目，并且已经具备大部分通用场景业务
是不是一个字？爽！
到此为止，你无需写任何一个代码！

**爽点**：

新人，看这里，项目下载之后直接可以启动，无任何依赖，之后你可以查看`Test控制器`，迫不及待的快来爽一爽！

我们将使用说明转移至我们的官方论坛中，正在制作中，尽情期待！

****
### 感谢：

**大力支持**： Eleven神、Sqlsugar上海杰哥、Gerry、哲学的老张

[橙子]https://ccnetcore.com

[lzw]https://github.com/yeslode

[朝夕教育]https://www.zhaoxiedu.net

[Sqlsugar]https://www.donet5.com/Home/Doc

[RuYiAdmin]https://gitee.com/pang-mingjun/RuYiAdmin

[ZrAdminNetCore]https://gitee.com/izory/ZrAdminNetCore

****
### 联系我们：

作者QQ：`454313500`，2029年之前作者24小时在线，时刻保持活跃更新。

QQ交流群：官方一群（已满）、官方二群（已满）、官方三群：`786308927`（加作者QQ后同意）

联系作者，这里人人都是顾问

官方网址：正在建设

****
### FQA:

问1：为什么不采用EFcore？

答1：别问，问就是Sqlsugar，和本框架一样爽！

问2：以后会持续更新下去吗？

答2：一定会的，我们的标题是 一个和Sqlsugar一样爽的.Net6开源框架 ，只要Sqlsugar在，我们将一直更新下去。

问3：这个框架的针对人群是哪些人？适合所有人吗？

答3：并不是适合所有人，应该算适合需要有一定基础的开发人员，当然，如果你是大神，你完全可以将这个框架二次开发！

问4：花如此多的精力制作这个框架，是为了什么？是为了赚钱吗？和目前主流的abp等框架比，又有什么意义呢？

答4：我们与Sqlsugar作者理念一致，我们是从用户角度出发，框架是为用户服务，与ABP复杂上手理念完全是相反的。

问5：为何不出版一个详细的说明书呢？

答5：暂时不会了，之后可能会，代码都是基于Asp.NetCore框架，适用于新手不用造轮子，整个框架较为简单，阅读源码后，基本能自定义改造使用了，过难也已经封装完毕，别忘了，其意义是为了开发更加简易！建议添加作者好友，这里人人都是顾问。

我大抵要厌倦了负重前行。

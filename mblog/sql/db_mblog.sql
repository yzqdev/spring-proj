/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : db_mblog

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 01/01/2023 18:55:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for flyway_schema_history
-- ----------------------------
DROP TABLE IF EXISTS `flyway_schema_history`;
CREATE TABLE `flyway_schema_history`  (
  `installed_rank` int NOT NULL,
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `script` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `checksum` int NULL DEFAULT NULL,
  `installed_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`) USING BTREE,
  INDEX `flyway_schema_history_s_idx`(`success` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flyway_schema_history
-- ----------------------------
INSERT INTO `flyway_schema_history` VALUES (1, '1', '<< Flyway Baseline >>', 'BASELINE', '<< Flyway Baseline >>', NULL, 'root', '2023-01-01 18:22:19', 0, 1);
INSERT INTO `flyway_schema_history` VALUES (2, '3.2', 'update', 'SQL', 'V3.2__update.sql', -53734780, 'root', '2023-01-01 18:22:20', 60, 1);

-- ----------------------------
-- Table structure for mto_channel
-- ----------------------------
DROP TABLE IF EXISTS `mto_channel`;
CREATE TABLE `mto_channel`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `key_` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `thumbnail` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `weight` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_channel
-- ----------------------------
INSERT INTO `mto_channel` VALUES (1, 'banner', 'banner', 1, '', 3);
INSERT INTO `mto_channel` VALUES (2, 'blog', '博客', 0, '', 2);
INSERT INTO `mto_channel` VALUES (3, 'jotting', '随笔', 0, '', 1);
INSERT INTO `mto_channel` VALUES (4, 'share', '分享', 0, '', 0);

-- ----------------------------
-- Table structure for mto_comment
-- ----------------------------
DROP TABLE IF EXISTS `mto_comment`;
CREATE TABLE `mto_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `author_id` bigint NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created` datetime NULL DEFAULT NULL,
  `pid` bigint NOT NULL,
  `post_id` bigint NULL DEFAULT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `IK_POST_ID`(`post_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_comment
-- ----------------------------

-- ----------------------------
-- Table structure for mto_favorite
-- ----------------------------
DROP TABLE IF EXISTS `mto_favorite`;
CREATE TABLE `mto_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created` datetime NULL DEFAULT NULL,
  `post_id` bigint NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `IK_USER_ID`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_favorite
-- ----------------------------

-- ----------------------------
-- Table structure for mto_links
-- ----------------------------
DROP TABLE IF EXISTS `mto_links`;
CREATE TABLE `mto_links`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_links
-- ----------------------------

-- ----------------------------
-- Table structure for mto_message
-- ----------------------------
DROP TABLE IF EXISTS `mto_message`;
CREATE TABLE `mto_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created` datetime NULL DEFAULT NULL,
  `event` int NOT NULL,
  `from_id` bigint NULL DEFAULT NULL,
  `post_id` bigint NULL DEFAULT NULL,
  `status` int NOT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_message
-- ----------------------------

-- ----------------------------
-- Table structure for mto_options
-- ----------------------------
DROP TABLE IF EXISTS `mto_options`;
CREATE TABLE `mto_options`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `key_` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` int NULL DEFAULT 0,
  `value` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_options
-- ----------------------------
INSERT INTO `mto_options` VALUES (1, 'site_name', 0, 'Mtons');
INSERT INTO `mto_options` VALUES (2, 'site_domain', 0, 'http://mtons.com');
INSERT INTO `mto_options` VALUES (3, 'site_keywords', 0, 'mtons,博客,社区');
INSERT INTO `mto_options` VALUES (4, 'site_description', 0, 'Mtons, 做一个有内涵的技术社区');
INSERT INTO `mto_options` VALUES (5, 'site_metas', 0, '');
INSERT INTO `mto_options` VALUES (6, 'site_copyright', 0, 'Copyright © Mtons');
INSERT INTO `mto_options` VALUES (7, 'site_icp', 0, '');
INSERT INTO `mto_options` VALUES (8, 'qq_callback', 0, '');
INSERT INTO `mto_options` VALUES (9, 'qq_app_id', 0, '');
INSERT INTO `mto_options` VALUES (10, 'qq_app_key', 0, '');
INSERT INTO `mto_options` VALUES (11, 'weibo_callback', 0, '');
INSERT INTO `mto_options` VALUES (12, 'weibo_client_id', 0, '');
INSERT INTO `mto_options` VALUES (13, 'weibo_client_sercret', 0, '');
INSERT INTO `mto_options` VALUES (14, 'github_callback', 0, '');
INSERT INTO `mto_options` VALUES (15, 'github_client_id', 0, '');
INSERT INTO `mto_options` VALUES (16, 'github_secret_key', 0, '');

-- ----------------------------
-- Table structure for mto_post
-- ----------------------------
DROP TABLE IF EXISTS `mto_post`;
CREATE TABLE `mto_post`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `author_id` bigint NULL DEFAULT NULL,
  `channel_id` int NULL DEFAULT NULL,
  `comments` int NOT NULL,
  `created` datetime NULL DEFAULT NULL,
  `favors` int NOT NULL,
  `featured` int NOT NULL,
  `status` int NOT NULL,
  `summary` varchar(140) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tags` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `thumbnail` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `views` int NOT NULL,
  `weight` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `IK_CHANNEL_ID`(`channel_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_post
-- ----------------------------
INSERT INTO `mto_post` VALUES (1, 1, 2, 0, '2023-01-01 18:50:21', 0, 0, 0, 'Mblog 开源Java博客系统, 支持多用户, 支持切换主题 技术选型： JDK8 MySQL Spring-boot Spring-data-jpa Shiro Lombok Freemarker Bootstrap SeaJs 启动： ma...', 'fff', '', 'aaa', 3, 0);

-- ----------------------------
-- Table structure for mto_post_attribute
-- ----------------------------
DROP TABLE IF EXISTS `mto_post_attribute`;
CREATE TABLE `mto_post_attribute`  (
  `id` bigint NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `editor` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'tinymce',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_post_attribute
-- ----------------------------
INSERT INTO `mto_post_attribute` VALUES (1, '### Mblog 开源Java博客系统, 支持多用户, 支持切换主题\r\n\r\n[![Author](https://img.shields.io/badge/author-landy-green.svg?style=flat-square)](http://mtons.com)\r\n[![JDK](https://img.shields.io/badge/jdk-1.8-green.svg?style=flat-square)](#)\r\n[![Release](https://img.shields.io/github/release/langhsu/mblog.svg?style=flat-square)](https://github.com/langhsu/mblog)\r\n[![license](https://img.shields.io/badge/license-GPL--3.0-green.svg)](https://github.com/langhsu/mblog/blob/master/LICENSE)\r\n[![Docker](https://img.shields.io/docker/automated/langhsu/mblog.svg?style=flat-square)](https://hub.docker.com/r/langhsu/mblog)\r\n[![QQ群](https://img.shields.io/badge/chat-Mtons-green.svg)](https://jq.qq.com/?_wv=1027&k=521CRdF)\r\n\r\n### 技术选型：\r\n\r\n* JDK8\r\n* MySQL\r\n* Spring-boot\r\n* Spring-data-jpa\r\n* Shiro\r\n* Lombok\r\n* Freemarker\r\n* Bootstrap\r\n* SeaJs\r\n\r\n### 启动：\r\n - main方法运行\r\n ```xml\r\n 配置：src/main/resources/application-mysql.yml (数据库账号密码)、新建db_mblog的数据库\r\n 运行：src/main/java/com/mtons/mblog/BootApplication\r\n 访问：http://localhost:8080/\r\n 后台：http://localhost:8080/admin\r\n 账号：默认管理员账号为 admin/12345\r\n \r\n TIPS: \r\n 如遇到启动失败/切换环境变量后启动失败的,请先maven clean后再尝试启动\r\n IDE得装lombok插件\r\n```\r\n\r\n- 文档: [说明文档](https://langhsu.github.io/mblog/#/)\r\n- 官网: [官网地址](http://www.mtons.com)\r\n- QQ交流群：378433412\r\n    \r\n### 版本(4.0)更新内容：\r\n    1. 新增 <@layout.extends name=\"xxx\"></layout.extends> 标签, 用于进入模板文件, 解决主题开发时各种路径带入主题名的问题\r\n    2. 新增 <@layout.block name=\"header\"></layout.block> 标签, 用于模板的占位, 可配合`layout.put`替换指定block区域内容,\r\n    3. 新增 <@layout.put block=\"contents\" type=\"APPEND\"></layout.put> 标签, 用户替换模板内容块, 丢弃freemarker变量传递, 增强主题可维护性\r\n    4. `layout.put`中的type 支持替换类型: APPEND, PREPEND, REPLACE\r\n    5. 调整`default`, `classic`主题, 使用新的主题开发方式\r\n    6. `建议MySQL版本5.7`, 如果不能满足5.7+可自行去除flyway依赖及代码\r\n    \r\n### 版本(3.5)更新内容：\r\n    1. 文件存储目录可配置, 见 site.location, 默认为 user.dir\r\n    2. 支持在${site.location}/storage/templates 目录下扩展自己的主题(${site.location}具体位置见启动日志)\r\n    3. 后台未配置对应第三方登录信息时, 前端不显示对应的按钮\r\n    4. 模板优化\r\n    5. 后台配置主题改为自动从目录中加载\r\n    6. 新增markdown编辑器, 可在后台选择tinymce/markdown\r\n    \r\n### 版本(3.0)更新内容：\r\n    1. 新增开关控制(注册开关, 发文开关, 评论开发)\r\n    2. 后台重写, 替换了所有后台页面功能更完善\r\n    3. 上传图片添加更多支持(本地/又拍云/阿里云/七牛云), 详情见后台系统配置\r\n    4. 升级为spring-boot2\r\n    5. 调整模板静态资源引用,方便扩展\r\n    6. 表名调整, 旧版本升级时请自行在数据库重命名, 详情见change.log\r\n    7. 重写了config(改为options), 可在applicaiton.yaml设置默认配置, 启动后将以options中配置为准\r\n    8. 支持后台设置主题\r\n    9. 去除了本地文件上传目录配置, 改为自动取项目运行目录(会在jar同级目录生成storeage和indexes目录)\r\n    10. 替换表单验证插件, 评论表情改为颜文字\r\n    11. 我的主页和Ta人主页合并\r\n    12. 优化了图片裁剪功能\r\n    13. 支持Docker, 详情见 https://hub.docker.com/r/langhsu/mblog\r\n    14. 邮件服务后台可配\r\n    15. 新增标签页\r\n    16. 新增注册邮箱验证开关(需要手动删除之前的 mto_security_code 表)\r\n      \r\n### 图片演示 \r\n\r\n ![输入图片说明](https://images.gitee.com/uploads/images/2019/0414/175116_449ed877_1758849.jpeg \"1.jpg\")\r\n ![输入图片说明](https://images.gitee.com/uploads/images/2019/0414/175353_6185e4f1_1758849.jpeg \"2.jpg\")\r\n\r\n### 扩展主题\r\n[Youth 主题传送门](https://pan.baidu.com/s/1tBwtprEAuCqcqDhVBunMGA) (作者:小崔崔)(提取码: 25e9)\r\n\r\n\r\n- 感谢开发主题大佬们的无私奉献\r\n', 'markdown');

-- ----------------------------
-- Table structure for mto_post_resource
-- ----------------------------
DROP TABLE IF EXISTS `mto_post_resource`;
CREATE TABLE `mto_post_resource`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `post_id` bigint NULL DEFAULT NULL,
  `resource_id` bigint NOT NULL,
  `sort` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `IK_R_POST_ID`(`post_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_post_resource
-- ----------------------------

-- ----------------------------
-- Table structure for mto_post_tag
-- ----------------------------
DROP TABLE IF EXISTS `mto_post_tag`;
CREATE TABLE `mto_post_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint NULL DEFAULT NULL,
  `tag_id` bigint NULL DEFAULT NULL,
  `weight` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `IK_TAG_ID`(`tag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_post_tag
-- ----------------------------
INSERT INTO `mto_post_tag` VALUES (1, 1, 1, 1672570275228);

-- ----------------------------
-- Table structure for mto_resource
-- ----------------------------
DROP TABLE IF EXISTS `mto_resource`;
CREATE TABLE `mto_resource`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint NOT NULL DEFAULT 0,
  `create_time` datetime NULL DEFAULT NULL,
  `md5` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_MD5`(`md5` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_resource
-- ----------------------------

-- ----------------------------
-- Table structure for mto_security_code
-- ----------------------------
DROP TABLE IF EXISTS `mto_security_code`;
CREATE TABLE `mto_security_code`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created` datetime NOT NULL,
  `expired` datetime NOT NULL,
  `key_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` int NULL DEFAULT NULL,
  `target` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_shxjkbkgnpxa80pnv4ts57o19`(`key_` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_security_code
-- ----------------------------

-- ----------------------------
-- Table structure for mto_tag
-- ----------------------------
DROP TABLE IF EXISTS `mto_tag`;
CREATE TABLE `mto_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created` datetime NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `latest_post_id` bigint NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `posts` int NOT NULL,
  `thumbnail` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `updated` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_9ki38gg59bw5agwxsc4xtednf`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_tag
-- ----------------------------
INSERT INTO `mto_tag` VALUES (1, '2023-01-01 18:50:22', NULL, 1, 'fff', 1, NULL, '2023-01-01 18:50:22');

-- ----------------------------
-- Table structure for mto_user
-- ----------------------------
DROP TABLE IF EXISTS `mto_user`;
CREATE TABLE `mto_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '/dist/images/ava/default.png',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL,
  `created` datetime NULL DEFAULT NULL,
  `updated` datetime NULL DEFAULT NULL,
  `last_login` datetime NULL DEFAULT NULL,
  `gender` int NOT NULL,
  `role_id` int NULL DEFAULT NULL,
  `comments` int NOT NULL,
  `posts` int NOT NULL,
  `signature` varchar(140) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_USERNAME`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_user
-- ----------------------------
INSERT INTO `mto_user` VALUES (1, 'admin', '小豆丁', '/storage/avatars/000/000/001_240.jpg', 'example@mtons.com', 'UUKHSDDI5KPA43A8VL06V0TU2', 0, '2017-08-06 17:52:41', '2017-07-26 11:08:36', '2023-01-01 18:54:52', 0, 1, 0, 1, '');

-- ----------------------------
-- Table structure for mto_user_oauth
-- ----------------------------
DROP TABLE IF EXISTS `mto_user_oauth`;
CREATE TABLE `mto_user_oauth`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `access_token` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `expire_in` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `oauth_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `oauth_type` int NULL DEFAULT NULL,
  `oauth_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `refresh_token` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_user_oauth
-- ----------------------------

-- ----------------------------
-- Table structure for shiro_permission
-- ----------------------------
DROP TABLE IF EXISTS `shiro_permission`;
CREATE TABLE `shiro_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(140) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `parent_id` bigint NULL DEFAULT NULL,
  `version` int NULL DEFAULT NULL,
  `weight` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_NAME`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shiro_permission
-- ----------------------------
INSERT INTO `shiro_permission` VALUES (1, '进入后台', 'admin', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (2, '栏目管理', 'channel:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (3, '编辑栏目', 'channel:update', 2, 0, 0);
INSERT INTO `shiro_permission` VALUES (4, '删除栏目', 'channel:delete', 2, 0, 0);
INSERT INTO `shiro_permission` VALUES (5, '文章管理', 'post:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (6, '编辑文章', 'post:update', 5, 0, 0);
INSERT INTO `shiro_permission` VALUES (7, '删除文章', 'post:delete', 5, 0, 0);
INSERT INTO `shiro_permission` VALUES (8, '评论管理', 'comment:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (9, '删除评论', 'comment:delete', 8, 0, 0);
INSERT INTO `shiro_permission` VALUES (10, '用户管理', 'user:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (11, '用户授权', 'user:role', 10, 0, 0);
INSERT INTO `shiro_permission` VALUES (12, '修改密码', 'user:pwd', 10, 0, 0);
INSERT INTO `shiro_permission` VALUES (13, '激活用户', 'user:open', 10, 0, 0);
INSERT INTO `shiro_permission` VALUES (14, '关闭用户', 'user:close', 10, 0, 0);
INSERT INTO `shiro_permission` VALUES (15, '角色管理', 'role:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (16, '修改角色', 'role:update', 15, 0, 0);
INSERT INTO `shiro_permission` VALUES (17, '删除角色', 'role:delete', 15, 0, 0);
INSERT INTO `shiro_permission` VALUES (18, '主题管理', 'theme:index', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (19, '系统配置', 'options:index', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (20, '修改配置', 'options:update', 19, 0, 0);

-- ----------------------------
-- Table structure for shiro_role
-- ----------------------------
DROP TABLE IF EXISTS `shiro_role`;
CREATE TABLE `shiro_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(140) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shiro_role
-- ----------------------------
INSERT INTO `shiro_role` VALUES (1, NULL, 'admin', 0);

-- ----------------------------
-- Table structure for shiro_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `shiro_role_permission`;
CREATE TABLE `shiro_role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `permission_id` bigint NULL DEFAULT NULL,
  `role_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shiro_role_permission
-- ----------------------------
INSERT INTO `shiro_role_permission` VALUES (1, 1, 1);
INSERT INTO `shiro_role_permission` VALUES (2, 2, 1);
INSERT INTO `shiro_role_permission` VALUES (3, 3, 1);
INSERT INTO `shiro_role_permission` VALUES (4, 4, 1);
INSERT INTO `shiro_role_permission` VALUES (5, 5, 1);
INSERT INTO `shiro_role_permission` VALUES (6, 6, 1);
INSERT INTO `shiro_role_permission` VALUES (7, 7, 1);
INSERT INTO `shiro_role_permission` VALUES (8, 8, 1);
INSERT INTO `shiro_role_permission` VALUES (9, 9, 1);
INSERT INTO `shiro_role_permission` VALUES (10, 10, 1);
INSERT INTO `shiro_role_permission` VALUES (11, 11, 1);
INSERT INTO `shiro_role_permission` VALUES (12, 12, 1);
INSERT INTO `shiro_role_permission` VALUES (13, 13, 1);
INSERT INTO `shiro_role_permission` VALUES (14, 14, 1);
INSERT INTO `shiro_role_permission` VALUES (15, 15, 1);
INSERT INTO `shiro_role_permission` VALUES (16, 16, 1);
INSERT INTO `shiro_role_permission` VALUES (17, 17, 1);
INSERT INTO `shiro_role_permission` VALUES (18, 18, 1);
INSERT INTO `shiro_role_permission` VALUES (19, 19, 1);
INSERT INTO `shiro_role_permission` VALUES (20, 20, 1);

-- ----------------------------
-- Table structure for shiro_user_role
-- ----------------------------
DROP TABLE IF EXISTS `shiro_user_role`;
CREATE TABLE `shiro_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shiro_user_role
-- ----------------------------
INSERT INTO `shiro_user_role` VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;

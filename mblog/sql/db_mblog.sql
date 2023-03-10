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
INSERT INTO `mto_channel` VALUES (2, 'blog', '??????', 0, '', 2);
INSERT INTO `mto_channel` VALUES (3, 'jotting', '??????', 0, '', 1);
INSERT INTO `mto_channel` VALUES (4, 'share', '??????', 0, '', 0);

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
INSERT INTO `mto_options` VALUES (3, 'site_keywords', 0, 'mtons,??????,??????');
INSERT INTO `mto_options` VALUES (4, 'site_description', 0, 'Mtons, ?????????????????????????????????');
INSERT INTO `mto_options` VALUES (5, 'site_metas', 0, '');
INSERT INTO `mto_options` VALUES (6, 'site_copyright', 0, 'Copyright ?? Mtons');
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
INSERT INTO `mto_post` VALUES (1, 1, 2, 0, '2023-01-01 18:50:21', 0, 0, 0, 'Mblog ??????Java????????????, ???????????????, ?????????????????? ??????????????? JDK8 MySQL Spring-boot Spring-data-jpa Shiro Lombok Freemarker Bootstrap SeaJs ????????? ma...', 'fff', '', 'aaa', 3, 0);

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
INSERT INTO `mto_post_attribute` VALUES (1, '### Mblog ??????Java????????????, ???????????????, ??????????????????\r\n\r\n[![Author](https://img.shields.io/badge/author-landy-green.svg?style=flat-square)](http://mtons.com)\r\n[![JDK](https://img.shields.io/badge/jdk-1.8-green.svg?style=flat-square)](#)\r\n[![Release](https://img.shields.io/github/release/langhsu/mblog.svg?style=flat-square)](https://github.com/langhsu/mblog)\r\n[![license](https://img.shields.io/badge/license-GPL--3.0-green.svg)](https://github.com/langhsu/mblog/blob/master/LICENSE)\r\n[![Docker](https://img.shields.io/docker/automated/langhsu/mblog.svg?style=flat-square)](https://hub.docker.com/r/langhsu/mblog)\r\n[![QQ???](https://img.shields.io/badge/chat-Mtons-green.svg)](https://jq.qq.com/?_wv=1027&k=521CRdF)\r\n\r\n### ???????????????\r\n\r\n* JDK8\r\n* MySQL\r\n* Spring-boot\r\n* Spring-data-jpa\r\n* Shiro\r\n* Lombok\r\n* Freemarker\r\n* Bootstrap\r\n* SeaJs\r\n\r\n### ?????????\r\n - main????????????\r\n ```xml\r\n ?????????src/main/resources/application-mysql.yml (?????????????????????)?????????db_mblog????????????\r\n ?????????src/main/java/com/mtons/mblog/BootApplication\r\n ?????????http://localhost:8080/\r\n ?????????http://localhost:8080/admin\r\n ????????????????????????????????? admin/12345\r\n \r\n TIPS: \r\n ?????????????????????/????????????????????????????????????,??????maven clean??????????????????\r\n IDE??????lombok??????\r\n```\r\n\r\n- ??????: [????????????](https://langhsu.github.io/mblog/#/)\r\n- ??????: [????????????](http://www.mtons.com)\r\n- QQ????????????378433412\r\n    \r\n### ??????(4.0)???????????????\r\n    1. ?????? <@layout.extends name=\"xxx\"></layout.extends> ??????, ????????????????????????, ?????????????????????????????????????????????????????????\r\n    2. ?????? <@layout.block name=\"header\"></layout.block> ??????, ?????????????????????, ?????????`layout.put`????????????block????????????,\r\n    3. ?????? <@layout.put block=\"contents\" type=\"APPEND\"></layout.put> ??????, ???????????????????????????, ??????freemarker????????????, ????????????????????????\r\n    4. `layout.put`??????type ??????????????????: APPEND, PREPEND, REPLACE\r\n    5. ??????`default`, `classic`??????, ??????????????????????????????\r\n    6. `??????MySQL??????5.7`, ??????????????????5.7+???????????????flyway???????????????\r\n    \r\n### ??????(3.5)???????????????\r\n    1. ???????????????????????????, ??? site.location, ????????? user.dir\r\n    2. ?????????${site.location}/storage/templates ??????????????????????????????(${site.location}???????????????????????????)\r\n    3. ?????????????????????????????????????????????, ??????????????????????????????\r\n    4. ????????????\r\n    5. ????????????????????????????????????????????????\r\n    6. ??????markdown?????????, ??????????????????tinymce/markdown\r\n    \r\n### ??????(3.0)???????????????\r\n    1. ??????????????????(????????????, ????????????, ????????????)\r\n    2. ????????????, ??????????????????????????????????????????\r\n    3. ??????????????????????????????(??????/?????????/?????????/?????????), ???????????????????????????\r\n    4. ?????????spring-boot2\r\n    5. ??????????????????????????????,????????????\r\n    6. ????????????, ????????????????????????????????????????????????, ?????????change.log\r\n    7. ?????????config(??????options), ??????applicaiton.yaml??????????????????, ???????????????options???????????????\r\n    8. ????????????????????????\r\n    9. ???????????????????????????????????????, ?????????????????????????????????(??????jar??????????????????storeage???indexes??????)\r\n    10. ????????????????????????, ???????????????????????????\r\n    11. ???????????????Ta???????????????\r\n    12. ???????????????????????????\r\n    13. ??????Docker, ????????? https://hub.docker.com/r/langhsu/mblog\r\n    14. ????????????????????????\r\n    15. ???????????????\r\n    16. ??????????????????????????????(??????????????????????????? mto_security_code ???)\r\n      \r\n### ???????????? \r\n\r\n ![??????????????????](https://images.gitee.com/uploads/images/2019/0414/175116_449ed877_1758849.jpeg \"1.jpg\")\r\n ![??????????????????](https://images.gitee.com/uploads/images/2019/0414/175353_6185e4f1_1758849.jpeg \"2.jpg\")\r\n\r\n### ????????????\r\n[Youth ???????????????](https://pan.baidu.com/s/1tBwtprEAuCqcqDhVBunMGA) (??????:?????????)(?????????: 25e9)\r\n\r\n\r\n- ??????????????????????????????????????????\r\n', 'markdown');

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
INSERT INTO `mto_user` VALUES (1, 'admin', '?????????', '/storage/avatars/000/000/001_240.jpg', 'example@mtons.com', 'UUKHSDDI5KPA43A8VL06V0TU2', 0, '2017-08-06 17:52:41', '2017-07-26 11:08:36', '2023-01-01 18:54:52', 0, 1, 0, 1, '');

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
INSERT INTO `shiro_permission` VALUES (1, '????????????', 'admin', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (2, '????????????', 'channel:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (3, '????????????', 'channel:update', 2, 0, 0);
INSERT INTO `shiro_permission` VALUES (4, '????????????', 'channel:delete', 2, 0, 0);
INSERT INTO `shiro_permission` VALUES (5, '????????????', 'post:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (6, '????????????', 'post:update', 5, 0, 0);
INSERT INTO `shiro_permission` VALUES (7, '????????????', 'post:delete', 5, 0, 0);
INSERT INTO `shiro_permission` VALUES (8, '????????????', 'comment:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (9, '????????????', 'comment:delete', 8, 0, 0);
INSERT INTO `shiro_permission` VALUES (10, '????????????', 'user:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (11, '????????????', 'user:role', 10, 0, 0);
INSERT INTO `shiro_permission` VALUES (12, '????????????', 'user:pwd', 10, 0, 0);
INSERT INTO `shiro_permission` VALUES (13, '????????????', 'user:open', 10, 0, 0);
INSERT INTO `shiro_permission` VALUES (14, '????????????', 'user:close', 10, 0, 0);
INSERT INTO `shiro_permission` VALUES (15, '????????????', 'role:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (16, '????????????', 'role:update', 15, 0, 0);
INSERT INTO `shiro_permission` VALUES (17, '????????????', 'role:delete', 15, 0, 0);
INSERT INTO `shiro_permission` VALUES (18, '????????????', 'theme:index', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (19, '????????????', 'options:index', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (20, '????????????', 'options:update', 19, 0, 0);

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

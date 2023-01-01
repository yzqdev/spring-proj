/*
 Navicat Premium Data Transfer

 Source Server         : localpg
 Source Server Type    : PostgreSQL
 Source Server Version : 140002
 Source Host           : localhost:5432
 Source Catalog        : tbed
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 140002
 File Encoding         : 65001

 Date: 16/05/2022 20:32:00
*/


-- ----------------------------
-- Table structure for album
-- ----------------------------
DROP TABLE IF EXISTS "public"."album";
CREATE TABLE "public"."album" (
  "album_key" varchar(255) COLLATE "pg_catalog"."default",
  "album_title" varchar(255) COLLATE "pg_catalog"."default",
  "password" varchar(255) COLLATE "pg_catalog"."default",
  "user_id" varchar(32) COLLATE "pg_catalog"."default",
  "username" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;

-- ----------------------------
-- Records of album
-- ----------------------------
INSERT INTO "public"."album" VALUES ('TOALBUMb7410N', '123', NULL, '1', NULL, '2022-05-16 20:08:27.228016', '2022-05-16 20:08:27.228016');

-- ----------------------------
-- Table structure for code
-- ----------------------------
DROP TABLE IF EXISTS "public"."code";
CREATE TABLE "public"."code" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "value" varchar(500) COLLATE "pg_catalog"."default",
  "expand_code" varchar(500) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Records of code
-- ----------------------------
INSERT INTO "public"."code" VALUES ('53a32aa53e6b8322a55b1a9aa3bb0467', '1073741824', 'c27ffa9ad0cc6f1ac0bf62a576c90c0b953a361fdcb0616cf481220935ee731f');

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS "public"."config";
CREATE TABLE "public"."config" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "sourcekey" int4,
  "emails" int4,
  "webname" varchar(255) COLLATE "pg_catalog"."default",
  "explain" varchar(1000) COLLATE "pg_catalog"."default",
  "video" varchar(1000) COLLATE "pg_catalog"."default",
  "backtype" varchar(500) COLLATE "pg_catalog"."default",
  "links" varchar(1000) COLLATE "pg_catalog"."default",
  "notice" varchar(1000) COLLATE "pg_catalog"."default",
  "baidu" varchar(1000) COLLATE "pg_catalog"."default",
  "domain" varchar(1000) COLLATE "pg_catalog"."default",
  "background1" varchar(1000) COLLATE "pg_catalog"."default",
  "background2" varchar(1000) COLLATE "pg_catalog"."default",
  "webms" varchar(1000) COLLATE "pg_catalog"."default",
  "webkeywords" varchar(1000) COLLATE "pg_catalog"."default",
  "webfavicons" varchar(1000) COLLATE "pg_catalog"."default",
  "theme" int4,
  "websubtitle" text COLLATE "pg_catalog"."default",
  "logo" text COLLATE "pg_catalog"."default",
  "about_info" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."config"."emails" IS '邮箱配置';
COMMENT ON COLUMN "public"."config"."webname" IS '网站名';
COMMENT ON COLUMN "public"."config"."backtype" IS '背景类型';
COMMENT ON COLUMN "public"."config"."theme" IS '主题';

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO "public"."config" VALUES ('1', 7, 1, 'Hellohao图床', 'Hellohao图像托管，是一家免费开源的图像托管，即时分享你的美好瞬间。', 'https://hellohao-cloud.oss-cn-beijing.aliyuncs.com/books_x264.mp4http://cdn.wwery.com/Hellohao/439550606022825.jpg', '1', '<p style="color:#7c7c88;">&copy; 2019 <a href="http://www.Hellohao.cn/" target="_blank" title="Hellohao">Hellohao</a><span>&nbsp;&nbsp;- All Rights Reserved</span> </p>', '也许...|这将是最好用的图床|为了更好的用户体验，建议您注册本站继续免费使用Hellohao图床。本站不得上传任何形式的非法图片，一旦发现，永久删除并禁封账户。情节严重者将相关资料交于相关部门处理。', 'console.log("内嵌的js代码")', 'http://localhost:8089', '', 'http://cdn.wwery.com/Hellohao/3e1901120091153.jpg', 'Hellohao图像托管，是一家免费开源的图像托管，即时分享你的美好瞬间。', 'hellohao图床,图床,图片上传,开源图床,hellohao,图像托管，图片分享', 'https://hellohao.nos-eastchina1.126.net/BlogImg/favicon.ico', 2, '这将是你用过最优秀的图像托管程序1', 'https://bbs.mihoyo.com/_nuxt/img/miHoYo_Game.a137b1d.png', '<img width="300px" src="http://img.wwery.com/Hellohao/rPscRYwz.png">
            <br />
            <br />
            <p>也许,这将是你用到最优秀的图像托管程序</p>
            <p>本程序为Hellohao图象托管程序</p>
            <br/>
            <p style="color: #656565;">作者：hellohao独立开发</p>
            <p style="color: #656565;">www.hellohao.cn</p>');

-- ----------------------------
-- Table structure for email_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."email_config";
CREATE TABLE "public"."email_config" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "emails" varchar(255) COLLATE "pg_catalog"."default",
  "email_key" varchar(255) COLLATE "pg_catalog"."default",
  "email_url" varchar(255) COLLATE "pg_catalog"."default",
  "port" varchar(255) COLLATE "pg_catalog"."default",
  "email_name" varchar(255) COLLATE "pg_catalog"."default",
  "using" int4
)
;
COMMENT ON COLUMN "public"."email_config"."emails" IS '邮箱';
COMMENT ON COLUMN "public"."email_config"."email_key" IS '授权码';
COMMENT ON COLUMN "public"."email_config"."email_url" IS '服务器';
COMMENT ON COLUMN "public"."email_config"."port" IS '端口';
COMMENT ON COLUMN "public"."email_config"."email_name" IS '用户名';
COMMENT ON COLUMN "public"."email_config"."using" IS '1为可用，其他为不使用';

-- ----------------------------
-- Records of email_config
-- ----------------------------

-- ----------------------------
-- Table structure for img_and_album
-- ----------------------------
DROP TABLE IF EXISTS "public"."img_and_album";
CREATE TABLE "public"."img_and_album" (
  "id" varchar(255) COLLATE "pg_catalog"."default",
  "img_name" varchar(255) COLLATE "pg_catalog"."default",
  "notes" varchar(1000) COLLATE "pg_catalog"."default",
  "album_key" varchar(32) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of img_and_album
-- ----------------------------
INSERT INTO "public"."img_and_album" VALUES ('admin/ytRbBq7K.jpg', 'TOALBUMb7410N', NULL, NULL);
INSERT INTO "public"."img_and_album" VALUES ('admin/OT0nI6R6.jpg', 'TOALBUMb7410N', NULL, NULL);
INSERT INTO "public"."img_and_album" VALUES ('admin/2SV2UEFZ.jpg', 'TOALBUMb7410N', NULL, NULL);
INSERT INTO "public"."img_and_album" VALUES ('admin/URyRzPdm.jpg', 'TOALBUMb7410N', NULL, NULL);

-- ----------------------------
-- Table structure for img_data
-- ----------------------------
DROP TABLE IF EXISTS "public"."img_data";
CREATE TABLE "public"."img_data" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "img_name" varchar(500) COLLATE "pg_catalog"."default",
  "img_url" varchar(500) COLLATE "pg_catalog"."default",
  "user_id" varchar(32) COLLATE "pg_catalog"."default",
  "sizes" int4,
  "abnormal" varchar(255) COLLATE "pg_catalog"."default",
  "source" varchar(32) COLLATE "pg_catalog"."default",
  "img_type" int4,
  "explains" varchar(500) COLLATE "pg_catalog"."default",
  "md5key" varchar(500) COLLATE "pg_catalog"."default",
  "img_uid" varchar(255) COLLATE "pg_catalog"."default",
  "format" varchar(255) COLLATE "pg_catalog"."default",
  "about" text COLLATE "pg_catalog"."default",
  "violation" varchar(50) COLLATE "pg_catalog"."default",
  "notes" varchar(255) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6),
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."img_data"."id" IS '主键';
COMMENT ON COLUMN "public"."img_data"."img_name" IS '图片名';
COMMENT ON COLUMN "public"."img_data"."img_url" IS '图片链接';
COMMENT ON COLUMN "public"."img_data"."user_id" IS '用户名';
COMMENT ON COLUMN "public"."img_data"."source" IS '存储源';
COMMENT ON COLUMN "public"."img_data"."update_time" IS '上传时间';
COMMENT ON COLUMN "public"."img_data"."create_time" IS '创建时间';

-- ----------------------------
-- Records of img_data
-- ----------------------------
INSERT INTO "public"."img_data" VALUES ('1526172681683677185', 'admin/OT0nI6R6.jpg', 'http://127.0.0.1:8089/ota/admin/OT0nI6R6.jpg', '1', 1608297, '0:0:0:0:0:0:0:1', '5', 0, NULL, 'a2f77bcbd9a915afcc91ba2bd32bae32', '932f876999f7457f9edb7e069a93c2b5', 'image/jpeg', NULL, NULL, NULL, '2022-05-16 20:08:16.877694', '2022-05-16 20:08:16.877694');
INSERT INTO "public"."img_data" VALUES ('1526172681683677186', 'admin/2SV2UEFZ.jpg', 'http://127.0.0.1:8089/ota/admin/2SV2UEFZ.jpg', '1', 3802376, '0:0:0:0:0:0:0:1', '5', 0, NULL, 'ac446decdb67317c2c9daf9b58c27da5', '446bd860f21745ca9cf6b95416f8bbd0', 'image/jpeg', NULL, NULL, NULL, '2022-05-16 20:08:16.889693', '2022-05-16 20:08:16.889693');
INSERT INTO "public"."img_data" VALUES ('1526172681725620225', 'admin/ytRbBq7K.jpg', 'http://127.0.0.1:8089/ota/admin/ytRbBq7K.jpg', '1', 4311695, '0:0:0:0:0:0:0:1', '5', 0, NULL, '9e555f1d6a77fce6ffd1d424ab05b8d0', '9991f911e65c44e58bf125fb22d179bf', 'image/jpeg', NULL, NULL, NULL, '2022-05-16 20:08:16.895688', '2022-05-16 20:08:16.895688');
INSERT INTO "public"."img_data" VALUES ('1526172683004882946', 'admin/URyRzPdm.jpg', 'http://127.0.0.1:8089/ota/admin/URyRzPdm.jpg', '1', 4367272, '0:0:0:0:0:0:0:1', '5', 0, NULL, '073bf3e75a0e581b898dbb392dd9dd04', '0f4ba85ea789424987394e69e93a29ff', 'image/jpeg', NULL, NULL, NULL, '2022-05-16 20:08:17.200695', '2022-05-16 20:08:17.200695');

-- ----------------------------
-- Table structure for img_temp
-- ----------------------------
DROP TABLE IF EXISTS "public"."img_temp";
CREATE TABLE "public"."img_temp" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "img_uid" varchar(255) COLLATE "pg_catalog"."default",
  "del_time" timestamptz(6)
)
;

-- ----------------------------
-- Records of img_temp
-- ----------------------------

-- ----------------------------
-- Table structure for imgreview
-- ----------------------------
DROP TABLE IF EXISTS "public"."imgreview";
CREATE TABLE "public"."imgreview" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(255) COLLATE "pg_catalog"."default",
  "api_key" varchar(255) COLLATE "pg_catalog"."default",
  "secret_key" varchar(255) COLLATE "pg_catalog"."default",
  "using" int4,
  "count" int4
)
;
COMMENT ON COLUMN "public"."imgreview"."count" IS '拦截数量';

-- ----------------------------
-- Records of imgreview
-- ----------------------------
INSERT INTO "public"."imgreview" VALUES ('1', '', '', '', 0, 0);

-- ----------------------------
-- Table structure for site_group
-- ----------------------------
DROP TABLE IF EXISTS "public"."site_group";
CREATE TABLE "public"."site_group" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "group_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "key_id" varchar(32) COLLATE "pg_catalog"."default",
  "user_type" int4,
  "compress" int4
)
;
COMMENT ON COLUMN "public"."site_group"."group_name" IS '组名称';

-- ----------------------------
-- Records of site_group
-- ----------------------------
INSERT INTO "public"."site_group" VALUES ('1', '默认群组', '5', 0, 0);

-- ----------------------------
-- Table structure for storage_key
-- ----------------------------
DROP TABLE IF EXISTS "public"."storage_key";
CREATE TABLE "public"."storage_key" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "access_key" varchar(255) COLLATE "pg_catalog"."default",
  "access_secret" varchar(255) COLLATE "pg_catalog"."default",
  "endpoint" varchar(255) COLLATE "pg_catalog"."default",
  "bucket_name" varchar(255) COLLATE "pg_catalog"."default",
  "request_address" varchar(255) COLLATE "pg_catalog"."default",
  "storage_type" int4,
  "key_name" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."storage_key"."key_name" IS '策略名称';

-- ----------------------------
-- Records of storage_key
-- ----------------------------
INSERT INTO "public"."storage_key" VALUES ('1', '', '', '', '', '', 2, '阿里云');
INSERT INTO "public"."storage_key" VALUES ('2', '', '', '', '', '', 1, '网易云');
INSERT INTO "public"."storage_key" VALUES ('3', '', '', '0', '', '', 3, '又拍');
INSERT INTO "public"."storage_key" VALUES ('4', '', '', '', '', '', 4, '七牛云');
INSERT INTO "public"."storage_key" VALUES ('5', '0', '0', '0', '0', 'http://127.0.0.1:8089', 5, 'Localhost');
INSERT INTO "public"."storage_key" VALUES ('6', '', '', '', '', '', 6, '腾讯云');
INSERT INTO "public"."storage_key" VALUES ('8', '', '', '0', '', '', 8, 'UFile');
INSERT INTO "public"."storage_key" VALUES ('13', '', '', '', '0', '', 7, 'ftp');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_config";
CREATE TABLE "public"."sys_config" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "register" int4 NOT NULL,
  "checkduplicate" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sys_config"."register" IS '是否可以注册';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO "public"."sys_config" VALUES ('1', 1, '0');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "username" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "email" varchar(50) COLLATE "pg_catalog"."default",
  "birthday" timestamp(6),
  "level" int4,
  "uid" varchar(50) COLLATE "pg_catalog"."default",
  "isok" int4 NOT NULL,
  "memory" float8,
  "group_id" varchar(32) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_user"."id" IS '主键';
COMMENT ON COLUMN "public"."sys_user"."username" IS '用户名';
COMMENT ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT ON COLUMN "public"."sys_user"."birthday" IS '生日';
COMMENT ON COLUMN "public"."sys_user"."level" IS '等级';
COMMENT ON COLUMN "public"."sys_user"."uid" IS '用户唯一标识';
COMMENT ON COLUMN "public"."sys_user"."create_time" IS '创建时间(注册事件)';
COMMENT ON COLUMN "public"."sys_user"."update_time" IS '更新时间';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user" VALUES ('1', 'admin', 'MTIzNDU2', '1@qq.com', '2019-01-10 00:00:00', 2, '54ee5d7ba60f4ff4bac28b88f15c6182', 1, 2147483648, '1', NULL, NULL);
INSERT INTO "public"."sys_user" VALUES ('0', 'tourist', '1', '1', '2022-05-23 09:33:12', 1, '1', 1, 2148532224, '1', '2022-05-16 09:33:23', '2022-05-16 09:33:27');

-- ----------------------------
-- Table structure for upload_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."upload_config";
CREATE TABLE "public"."upload_config" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "file_size_tourists" varchar(500) COLLATE "pg_catalog"."default",
  "file_size_user" varchar(500) COLLATE "pg_catalog"."default",
  "img_count_tourists" int4,
  "img_count_user" int4,
  "suffix" varchar(255) COLLATE "pg_catalog"."default",
  "url_type" int4,
  "is_update" int4,
  "api" int4 NOT NULL,
  "visitor_storage" varchar(500) COLLATE "pg_catalog"."default",
  "user_storage" varchar(500) COLLATE "pg_catalog"."default",
  "blacklist" varchar(4000) COLLATE "pg_catalog"."default",
  "userclose" int4
)
;
COMMENT ON COLUMN "public"."upload_config"."img_count_tourists" IS '游客文件总数量, 超出则不允许加入队列';
COMMENT ON COLUMN "public"."upload_config"."img_count_user" IS '用户文件总数量, 超出则不允许加入队列';
COMMENT ON COLUMN "public"."upload_config"."suffix" IS '支持后缀';
COMMENT ON COLUMN "public"."upload_config"."url_type" IS 'url类型';
COMMENT ON COLUMN "public"."upload_config"."is_update" IS '禁止游客上传';
COMMENT ON COLUMN "public"."upload_config"."api" IS '开启api';
COMMENT ON COLUMN "public"."upload_config"."userclose" IS '用户上传开关';

-- ----------------------------
-- Records of upload_config
-- ----------------------------
INSERT INTO "public"."upload_config" VALUES ('1', '10485760', '20971520', 5, 10, 'gif,jpg,jpeg,bmp,png,jfif,webp', 1, 1, 1, '524288000', '104857600', '', 1);

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS "public"."user_group";
CREATE TABLE "public"."user_group" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" varchar(32) COLLATE "pg_catalog"."default",
  "group_id" varchar(32) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of user_group
-- ----------------------------
INSERT INTO "public"."user_group" VALUES ('1', '1', '1');

-- ----------------------------
-- Primary Key structure for table code
-- ----------------------------
ALTER TABLE "public"."code" ADD CONSTRAINT "code_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table config
-- ----------------------------
ALTER TABLE "public"."config" ADD CONSTRAINT "config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table email_config
-- ----------------------------
ALTER TABLE "public"."email_config" ADD CONSTRAINT "email_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table img_data
-- ----------------------------
CREATE INDEX "index_md5key_url" ON "public"."img_data" USING btree (
  "md5key" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "img_url" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table img_data
-- ----------------------------
ALTER TABLE "public"."img_data" ADD CONSTRAINT "img_data_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table img_temp
-- ----------------------------
ALTER TABLE "public"."img_temp" ADD CONSTRAINT "img_temp_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table imgreview
-- ----------------------------
ALTER TABLE "public"."imgreview" ADD CONSTRAINT "imgreview_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table site_group
-- ----------------------------
ALTER TABLE "public"."site_group" ADD CONSTRAINT "site_group_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table storage_key
-- ----------------------------
ALTER TABLE "public"."storage_key" ADD CONSTRAINT "storage_key_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_config
-- ----------------------------
ALTER TABLE "public"."sys_config" ADD CONSTRAINT "sysconfig_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table upload_config
-- ----------------------------
ALTER TABLE "public"."upload_config" ADD CONSTRAINT "upload_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table user_group
-- ----------------------------
ALTER TABLE "public"."user_group" ADD CONSTRAINT "user_group_pkey" PRIMARY KEY ("id");

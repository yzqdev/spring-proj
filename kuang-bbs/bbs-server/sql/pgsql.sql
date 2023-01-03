/*
 Navicat Premium Data Transfer

 Source Server         : localpg
 Source Server Type    : PostgreSQL
 Source Server Version : 140002
 Source Host           : localhost:5432
 Source Catalog        : kuangstudy
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 140002
 File Encoding         : 65001

 Date: 17/05/2022 04:21:36
*/


-- ----------------------------
-- Table structure for ks_blog
-- ----------------------------
DROP TABLE IF EXISTS "public"."ks_blog";
CREATE TABLE "public"."ks_blog" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "bid" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "title" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default" NOT NULL,
  "sort" int4 NOT NULL,
  "views" int4 NOT NULL,
  "author_id" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "author_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "author_avatar" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "category_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "category_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "gmt_create" timestamp(6) NOT NULL,
  "gmt_update" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."ks_blog"."id" IS '自增id';
COMMENT ON COLUMN "public"."ks_blog"."bid" IS '博客id';
COMMENT ON COLUMN "public"."ks_blog"."title" IS '博客标题';
COMMENT ON COLUMN "public"."ks_blog"."content" IS '博客内容';
COMMENT ON COLUMN "public"."ks_blog"."sort" IS '排序 0 普通  1 置顶';
COMMENT ON COLUMN "public"."ks_blog"."views" IS '浏览量';
COMMENT ON COLUMN "public"."ks_blog"."author_id" IS '作者id';
COMMENT ON COLUMN "public"."ks_blog"."author_name" IS '作者名';
COMMENT ON COLUMN "public"."ks_blog"."author_avatar" IS '作者头像';
COMMENT ON COLUMN "public"."ks_blog"."category_id" IS '问题分类id';
COMMENT ON COLUMN "public"."ks_blog"."category_name" IS '问题分类名称';
COMMENT ON COLUMN "public"."ks_blog"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "public"."ks_blog"."gmt_update" IS '修改时间';

-- ----------------------------
-- Records of ks_blog
-- ----------------------------
INSERT INTO "public"."ks_blog" VALUES ('30', '9b04d32210d24b1397e944fe87453157', '沙发的说法', ' 舒服', 0, 5, '464a084dd56a45a69a735a4a2e3f0db3', 'yzqdev', '/images/avatar/avatar-1.jpg', '1', '222', '2021-11-21 22:12:44', '2021-11-21 22:20:53');

-- ----------------------------
-- Table structure for ks_blog_category
-- ----------------------------
DROP TABLE IF EXISTS "public"."ks_blog_category";
CREATE TABLE "public"."ks_blog_category" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "category" varchar(50) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."ks_blog_category"."id" IS '自增id';
COMMENT ON COLUMN "public"."ks_blog_category"."category" IS '博客分类';

-- ----------------------------
-- Records of ks_blog_category
-- ----------------------------
INSERT INTO "public"."ks_blog_category" VALUES ('1', '222');

-- ----------------------------
-- Table structure for ks_comment
-- ----------------------------
DROP TABLE IF EXISTS "public"."ks_comment";
CREATE TABLE "public"."ks_comment" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "comment_id" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "topic_category" int4 NOT NULL,
  "topic_id" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "user_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "user_avatar" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default" NOT NULL,
  "gmt_create" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."ks_comment"."id" IS '自增id';
COMMENT ON COLUMN "public"."ks_comment"."comment_id" IS '评论唯一id';
COMMENT ON COLUMN "public"."ks_comment"."topic_category" IS '1博客 2问答';
COMMENT ON COLUMN "public"."ks_comment"."topic_id" IS '评论主题id';
COMMENT ON COLUMN "public"."ks_comment"."user_id" IS '评论者id';
COMMENT ON COLUMN "public"."ks_comment"."user_name" IS '评论者昵称';
COMMENT ON COLUMN "public"."ks_comment"."user_avatar" IS '评论者头像';
COMMENT ON COLUMN "public"."ks_comment"."content" IS '评论内容';
COMMENT ON COLUMN "public"."ks_comment"."gmt_create" IS '评论创建时间';

-- ----------------------------
-- Records of ks_comment
-- ----------------------------
INSERT INTO "public"."ks_comment" VALUES ('137', 'db3ed344ef2c489ba701a17edbae9ad0', 2, 'fb850442eba54bcdb0111a8395e898db', '464a084dd56a45a69a735a4a2e3f0db3', 'yzqdev', '/images/avatar/avatar-1.jpg', '方法', '2021-11-21 22:11:38');

-- ----------------------------
-- Table structure for ks_download
-- ----------------------------
DROP TABLE IF EXISTS "public"."ks_download";
CREATE TABLE "public"."ks_download" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "dname" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "ddesc" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "dcode" varchar(50) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."ks_download"."dname" IS '资源名';
COMMENT ON COLUMN "public"."ks_download"."ddesc" IS '资源链接';
COMMENT ON COLUMN "public"."ks_download"."dcode" IS '提取码';

-- ----------------------------
-- Records of ks_download
-- ----------------------------

-- ----------------------------
-- Table structure for ks_invite
-- ----------------------------
DROP TABLE IF EXISTS "public"."ks_invite";
CREATE TABLE "public"."ks_invite" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "code" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "uid" varchar(200) COLLATE "pg_catalog"."default",
  "status" int4 NOT NULL,
  "active_time" timestamp(6),
  "gmt_create" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."ks_invite"."id" IS '自增id';
COMMENT ON COLUMN "public"."ks_invite"."code" IS '邀请码';
COMMENT ON COLUMN "public"."ks_invite"."uid" IS '用户id';
COMMENT ON COLUMN "public"."ks_invite"."status" IS '状态 0 未使用 1 使用';
COMMENT ON COLUMN "public"."ks_invite"."active_time" IS '激活时间';
COMMENT ON COLUMN "public"."ks_invite"."gmt_create" IS '创建时间';

-- ----------------------------
-- Records of ks_invite
-- ----------------------------
INSERT INTO "public"."ks_invite" VALUES ('1', '111', 'aaa', 1, '2022-05-17 04:20:54.771', '2021-11-28 22:04:20');

-- ----------------------------
-- Table structure for ks_question
-- ----------------------------
DROP TABLE IF EXISTS "public"."ks_question";
CREATE TABLE "public"."ks_question" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "qid" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "title" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default" NOT NULL,
  "status" int4 NOT NULL,
  "sort" int4 NOT NULL,
  "views" int4 NOT NULL,
  "author_id" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "author_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "author_avatar" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "category_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "category_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "gmt_create" timestamp(6) NOT NULL,
  "gmt_update" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."ks_question"."id" IS '自增id';
COMMENT ON COLUMN "public"."ks_question"."qid" IS '问题id';
COMMENT ON COLUMN "public"."ks_question"."title" IS '问题标题';
COMMENT ON COLUMN "public"."ks_question"."content" IS '问题内容';
COMMENT ON COLUMN "public"."ks_question"."status" IS '状态 0 未解决 1 已解决';
COMMENT ON COLUMN "public"."ks_question"."sort" IS '排序 0 普通  1 置顶';
COMMENT ON COLUMN "public"."ks_question"."views" IS '浏览量';
COMMENT ON COLUMN "public"."ks_question"."author_id" IS '作者id';
COMMENT ON COLUMN "public"."ks_question"."author_name" IS '作者名';
COMMENT ON COLUMN "public"."ks_question"."author_avatar" IS '作者头像';
COMMENT ON COLUMN "public"."ks_question"."category_id" IS '问题分类id';
COMMENT ON COLUMN "public"."ks_question"."category_name" IS '问题分类名称';
COMMENT ON COLUMN "public"."ks_question"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "public"."ks_question"."gmt_update" IS '修改时间';

-- ----------------------------
-- Records of ks_question
-- ----------------------------
INSERT INTO "public"."ks_question" VALUES ('35', 'fb850442eba54bcdb0111a8395e898db', '舒服舒服', ' 舒服舒服', 1, 0, 6, '464a084dd56a45a69a735a4a2e3f0db3', 'yzqdev', '/images/avatar/avatar-1.jpg', '1', '222', '2021-11-21 22:11:33', '2021-11-21 22:21:55');

-- ----------------------------
-- Table structure for ks_question_category
-- ----------------------------
DROP TABLE IF EXISTS "public"."ks_question_category";
CREATE TABLE "public"."ks_question_category" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "category" varchar(50) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."ks_question_category"."id" IS '自增id';
COMMENT ON COLUMN "public"."ks_question_category"."category" IS '问题分类';

-- ----------------------------
-- Records of ks_question_category
-- ----------------------------
INSERT INTO "public"."ks_question_category" VALUES ('1', '222');

-- ----------------------------
-- Table structure for ks_say
-- ----------------------------
DROP TABLE IF EXISTS "public"."ks_say";
CREATE TABLE "public"."ks_say" (
  "id" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "title" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "content" varchar(5000) COLLATE "pg_catalog"."default" NOT NULL,
  "gmt_create" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."ks_say"."id" IS '唯一id';
COMMENT ON COLUMN "public"."ks_say"."title" IS '标题';
COMMENT ON COLUMN "public"."ks_say"."content" IS '内容';
COMMENT ON COLUMN "public"."ks_say"."gmt_create" IS '时间';

-- ----------------------------
-- Records of ks_say
-- ----------------------------

-- ----------------------------
-- Table structure for ks_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."ks_user";
CREATE TABLE "public"."ks_user" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "role_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "username" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "avatar" varchar(500) COLLATE "pg_catalog"."default",
  "login_date" timestamp(6),
  "gmt_create" timestamp(6)
)
;
COMMENT ON COLUMN "public"."ks_user"."id" IS '自增id';
COMMENT ON COLUMN "public"."ks_user"."user_id" IS '用户编号';
COMMENT ON COLUMN "public"."ks_user"."role_id" IS '角色编号';
COMMENT ON COLUMN "public"."ks_user"."username" IS '用户名';
COMMENT ON COLUMN "public"."ks_user"."password" IS '密码';
COMMENT ON COLUMN "public"."ks_user"."avatar" IS '头像';
COMMENT ON COLUMN "public"."ks_user"."login_date" IS '登录时间';
COMMENT ON COLUMN "public"."ks_user"."gmt_create" IS '创建时间';

-- ----------------------------
-- Records of ks_user
-- ----------------------------
INSERT INTO "public"."ks_user" VALUES ('1', '4a6c46eda2254985818e4c8d08a9b879', '2', 'string', 'string', '/images/avatar/avatar-1.jpg', '2021-11-22 16:32:46', '2021-11-22 16:32:46');
INSERT INTO "public"."ks_user" VALUES ('2', 'aaa', '2', 'yzq', '123', '/images/avatar/avatar-1.jpg', '2021-11-22 18:17:56', '2021-11-22 18:17:56');
INSERT INTO "public"."ks_user" VALUES ('1526296656409620482', 'aaa', '2', 'stringaaff', 'string', NULL, '2022-05-17 04:20:54.766', '2022-05-17 04:20:54.766');

-- ----------------------------
-- Table structure for ks_user_donate
-- ----------------------------
DROP TABLE IF EXISTS "public"."ks_user_donate";
CREATE TABLE "public"."ks_user_donate" (
  "uid" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "donate_json" varchar(2000) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."ks_user_donate"."uid" IS '用户id';
COMMENT ON COLUMN "public"."ks_user_donate"."donate_json" IS '赞赏二维码信息';

-- ----------------------------
-- Records of ks_user_donate
-- ----------------------------

-- ----------------------------
-- Table structure for ks_user_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."ks_user_info";
CREATE TABLE "public"."ks_user_info" (
  "uid" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "nickname" varchar(80) COLLATE "pg_catalog"."default",
  "realname" varchar(80) COLLATE "pg_catalog"."default",
  "qq" varchar(20) COLLATE "pg_catalog"."default",
  "wechat" varchar(200) COLLATE "pg_catalog"."default",
  "email" varchar(500) COLLATE "pg_catalog"."default",
  "phone" varchar(20) COLLATE "pg_catalog"."default",
  "work" varchar(200) COLLATE "pg_catalog"."default",
  "address" varchar(500) COLLATE "pg_catalog"."default",
  "hobby" varchar(500) COLLATE "pg_catalog"."default",
  "intro" varchar(2000) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."ks_user_info"."uid" IS '用户id';
COMMENT ON COLUMN "public"."ks_user_info"."nickname" IS '用户昵称';
COMMENT ON COLUMN "public"."ks_user_info"."realname" IS '真实姓名';
COMMENT ON COLUMN "public"."ks_user_info"."qq" IS 'QQ';
COMMENT ON COLUMN "public"."ks_user_info"."wechat" IS 'WeChat';
COMMENT ON COLUMN "public"."ks_user_info"."email" IS '邮箱';
COMMENT ON COLUMN "public"."ks_user_info"."phone" IS '手机';
COMMENT ON COLUMN "public"."ks_user_info"."work" IS '工作';
COMMENT ON COLUMN "public"."ks_user_info"."address" IS '地址';
COMMENT ON COLUMN "public"."ks_user_info"."hobby" IS '爱好';
COMMENT ON COLUMN "public"."ks_user_info"."intro" IS '自我介绍';

-- ----------------------------
-- Records of ks_user_info
-- ----------------------------
INSERT INTO "public"."ks_user_info" VALUES ('aaa', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."ks_user_info" VALUES ('aaa', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for ks_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."ks_user_role";
CREATE TABLE "public"."ks_user_role" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "gmt_create" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."ks_user_role"."id" IS '角色编号';
COMMENT ON COLUMN "public"."ks_user_role"."name" IS '角色名称';
COMMENT ON COLUMN "public"."ks_user_role"."description" IS '角色描述';
COMMENT ON COLUMN "public"."ks_user_role"."gmt_create" IS '创建时间';

-- ----------------------------
-- Records of ks_user_role
-- ----------------------------
INSERT INTO "public"."ks_user_role" VALUES ('2', '11', '无描述...', '2021-11-21 22:05:44');

-- ----------------------------
-- Primary Key structure for table ks_blog
-- ----------------------------
ALTER TABLE "public"."ks_blog" ADD CONSTRAINT "ks_blog_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ks_blog_category
-- ----------------------------
ALTER TABLE "public"."ks_blog_category" ADD CONSTRAINT "ks_blog_category_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ks_comment
-- ----------------------------
ALTER TABLE "public"."ks_comment" ADD CONSTRAINT "ks_comment_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ks_download
-- ----------------------------
ALTER TABLE "public"."ks_download" ADD CONSTRAINT "ks_download_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ks_invite
-- ----------------------------
ALTER TABLE "public"."ks_invite" ADD CONSTRAINT "ks_invite_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ks_question
-- ----------------------------
ALTER TABLE "public"."ks_question" ADD CONSTRAINT "ks_question_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ks_question_category
-- ----------------------------
ALTER TABLE "public"."ks_question_category" ADD CONSTRAINT "ks_question_category_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ks_say
-- ----------------------------
ALTER TABLE "public"."ks_say" ADD CONSTRAINT "ks_say_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ks_user
-- ----------------------------
ALTER TABLE "public"."ks_user" ADD CONSTRAINT "ks_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ks_user_role
-- ----------------------------
ALTER TABLE "public"."ks_user_role" ADD CONSTRAINT "ks_user_role_pkey" PRIMARY KEY ("id");

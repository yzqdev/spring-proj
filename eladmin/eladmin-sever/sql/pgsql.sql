/*
 Navicat Premium Data Transfer

 Source Server         : localpg
 Source Server Type    : PostgreSQL
 Source Server Version : 140002
 Source Host           : localhost:5432
 Source Catalog        : eladmin
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 140002
 File Encoding         : 65001

 Date: 10/06/2022 22:38:47
*/


-- ----------------------------
-- Table structure for code_column_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."code_column_config";
CREATE TABLE "public"."code_column_config" (
  "column_id" int8 NOT NULL,
  "table_name" varchar(255) COLLATE "pg_catalog"."default",
  "column_name" varchar(255) COLLATE "pg_catalog"."default",
  "column_type" varchar(255) COLLATE "pg_catalog"."default",
  "dict_name" varchar(255) COLLATE "pg_catalog"."default",
  "extra" varchar(255) COLLATE "pg_catalog"."default",
  "form_show" varchar(1) COLLATE "pg_catalog"."default",
  "form_type" varchar(255) COLLATE "pg_catalog"."default",
  "key_type" varchar(255) COLLATE "pg_catalog"."default",
  "list_show" varchar(1) COLLATE "pg_catalog"."default",
  "not_null" varchar(1) COLLATE "pg_catalog"."default",
  "query_type" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "date_annotation" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."code_column_config"."column_id" IS 'ID';
COMMENT ON TABLE "public"."code_column_config" IS '代码生成字段信息存储';

-- ----------------------------
-- Records of code_column_config
-- ----------------------------
INSERT INTO "public"."code_column_config" VALUES (191, 'tool_qiniu_content', 'content_id', 'bigint', NULL, 'auto_increment', '1', NULL, 'PRI', '1', '0', NULL, 'ID', NULL);
INSERT INTO "public"."code_column_config" VALUES (192, 'tool_qiniu_content', 'bucket', 'varchar', NULL, '', '1', NULL, '', '1', '0', NULL, 'Bucket 识别符', NULL);
INSERT INTO "public"."code_column_config" VALUES (193, 'tool_qiniu_content', 'name', 'varchar', NULL, '', '1', NULL, 'UNI', '1', '0', NULL, '文件名称', NULL);
INSERT INTO "public"."code_column_config" VALUES (194, 'tool_qiniu_content', 'size', 'varchar', NULL, '', '1', NULL, '', '1', '0', NULL, '文件大小', NULL);
INSERT INTO "public"."code_column_config" VALUES (195, 'tool_qiniu_content', 'type', 'varchar', NULL, '', '1', NULL, '', '1', '0', NULL, '文件类型：私有或公开', NULL);
INSERT INTO "public"."code_column_config" VALUES (196, 'tool_qiniu_content', 'url', 'varchar', NULL, '', '1', NULL, '', '1', '0', NULL, '文件url', NULL);
INSERT INTO "public"."code_column_config" VALUES (197, 'tool_qiniu_content', 'suffix', 'varchar', NULL, '', '1', NULL, '', '1', '0', NULL, '文件后缀', NULL);
INSERT INTO "public"."code_column_config" VALUES (198, 'tool_qiniu_content', 'update_time', 'datetime', NULL, '', '1', NULL, '', '1', '0', NULL, '上传或同步的时间', NULL);

-- ----------------------------
-- Table structure for code_gen_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."code_gen_config";
CREATE TABLE "public"."code_gen_config" (
  "config_id" int8 NOT NULL,
  "table_name" varchar(255) COLLATE "pg_catalog"."default",
  "author" varchar(255) COLLATE "pg_catalog"."default",
  "cover" varchar(1) COLLATE "pg_catalog"."default",
  "module_name" varchar(255) COLLATE "pg_catalog"."default",
  "pack" varchar(255) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "api_path" varchar(255) COLLATE "pg_catalog"."default",
  "prefix" varchar(255) COLLATE "pg_catalog"."default",
  "api_alias" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."code_gen_config"."config_id" IS 'ID';
COMMENT ON COLUMN "public"."code_gen_config"."table_name" IS '表名';
COMMENT ON COLUMN "public"."code_gen_config"."author" IS '作者';
COMMENT ON COLUMN "public"."code_gen_config"."cover" IS '是否覆盖';
COMMENT ON COLUMN "public"."code_gen_config"."module_name" IS '模块名称';
COMMENT ON COLUMN "public"."code_gen_config"."pack" IS '至于哪个包下';
COMMENT ON COLUMN "public"."code_gen_config"."path" IS '前端代码生成的路径';
COMMENT ON COLUMN "public"."code_gen_config"."api_path" IS '前端Api文件路径';
COMMENT ON COLUMN "public"."code_gen_config"."prefix" IS '表前缀';
COMMENT ON COLUMN "public"."code_gen_config"."api_alias" IS '接口名称';
COMMENT ON TABLE "public"."code_gen_config" IS '代码生成器配置';

-- ----------------------------
-- Records of code_gen_config
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."mnt_app";
CREATE TABLE "public"."mnt_app" (
  "app_id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "upload_path" varchar(255) COLLATE "pg_catalog"."default",
  "deploy_path" varchar(255) COLLATE "pg_catalog"."default",
  "backup_path" varchar(255) COLLATE "pg_catalog"."default",
  "port" int4,
  "start_script" varchar(4000) COLLATE "pg_catalog"."default",
  "deploy_script" varchar(4000) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."mnt_app"."app_id" IS 'ID';
COMMENT ON COLUMN "public"."mnt_app"."name" IS '应用名称';
COMMENT ON COLUMN "public"."mnt_app"."upload_path" IS '上传目录';
COMMENT ON COLUMN "public"."mnt_app"."deploy_path" IS '部署路径';
COMMENT ON COLUMN "public"."mnt_app"."backup_path" IS '备份路径';
COMMENT ON COLUMN "public"."mnt_app"."port" IS '应用端口';
COMMENT ON COLUMN "public"."mnt_app"."start_script" IS '启动脚本';
COMMENT ON COLUMN "public"."mnt_app"."deploy_script" IS '部署脚本';
COMMENT ON COLUMN "public"."mnt_app"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."mnt_app"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."mnt_app"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."mnt_app"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."mnt_app" IS '应用管理';

-- ----------------------------
-- Records of mnt_app
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_database
-- ----------------------------
DROP TABLE IF EXISTS "public"."mnt_database";
CREATE TABLE "public"."mnt_database" (
  "db_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "jdbc_url" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "user_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "pwd" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."mnt_database"."db_id" IS 'ID';
COMMENT ON COLUMN "public"."mnt_database"."name" IS '名称';
COMMENT ON COLUMN "public"."mnt_database"."jdbc_url" IS 'jdbc连接';
COMMENT ON COLUMN "public"."mnt_database"."user_name" IS '账号';
COMMENT ON COLUMN "public"."mnt_database"."pwd" IS '密码';
COMMENT ON COLUMN "public"."mnt_database"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."mnt_database"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."mnt_database"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mnt_database"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."mnt_database" IS '数据库管理';

-- ----------------------------
-- Records of mnt_database
-- ----------------------------
INSERT INTO "public"."mnt_database" VALUES ('76ede40de26e4f28ac295e6681e769fe', 'eladmin', 'jdbc:mysql://localhost:3306/eladmin', 'root', '123456', 'admin', 'admin', '2021-08-31 14:30:34', '2022-05-18 15:40:53.339');

-- ----------------------------
-- Table structure for mnt_deploy
-- ----------------------------
DROP TABLE IF EXISTS "public"."mnt_deploy";
CREATE TABLE "public"."mnt_deploy" (
  "deploy_id" int8 NOT NULL,
  "app_id" int8,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."mnt_deploy"."deploy_id" IS 'ID';
COMMENT ON COLUMN "public"."mnt_deploy"."app_id" IS '应用编号';
COMMENT ON COLUMN "public"."mnt_deploy"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."mnt_deploy"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."mnt_deploy"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."mnt_deploy" IS '部署管理';

-- ----------------------------
-- Records of mnt_deploy
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_deploy_history
-- ----------------------------
DROP TABLE IF EXISTS "public"."mnt_deploy_history";
CREATE TABLE "public"."mnt_deploy_history" (
  "history_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "deploy_date" timestamp(6) NOT NULL,
  "deploy_user" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "ip" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "deploy_id" int8
)
;
COMMENT ON COLUMN "public"."mnt_deploy_history"."history_id" IS 'ID';
COMMENT ON COLUMN "public"."mnt_deploy_history"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."mnt_deploy_history"."deploy_date" IS '部署日期';
COMMENT ON COLUMN "public"."mnt_deploy_history"."deploy_user" IS '部署用户';
COMMENT ON COLUMN "public"."mnt_deploy_history"."ip" IS '服务器IP';
COMMENT ON COLUMN "public"."mnt_deploy_history"."deploy_id" IS '部署编号';
COMMENT ON TABLE "public"."mnt_deploy_history" IS '部署历史管理';

-- ----------------------------
-- Records of mnt_deploy_history
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_deploy_server
-- ----------------------------
DROP TABLE IF EXISTS "public"."mnt_deploy_server";
CREATE TABLE "public"."mnt_deploy_server" (
  "deploy_id" int8 NOT NULL,
  "server_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."mnt_deploy_server"."deploy_id" IS '部署ID';
COMMENT ON COLUMN "public"."mnt_deploy_server"."server_id" IS '服务ID';
COMMENT ON TABLE "public"."mnt_deploy_server" IS '应用与服务器关联';

-- ----------------------------
-- Records of mnt_deploy_server
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_server
-- ----------------------------
DROP TABLE IF EXISTS "public"."mnt_server";
CREATE TABLE "public"."mnt_server" (
  "server_id" int8 NOT NULL,
  "account" varchar(50) COLLATE "pg_catalog"."default",
  "ip" varchar(20) COLLATE "pg_catalog"."default",
  "name" varchar(100) COLLATE "pg_catalog"."default",
  "password" varchar(100) COLLATE "pg_catalog"."default",
  "port" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."mnt_server"."server_id" IS 'ID';
COMMENT ON COLUMN "public"."mnt_server"."account" IS '账号';
COMMENT ON COLUMN "public"."mnt_server"."ip" IS 'IP地址';
COMMENT ON COLUMN "public"."mnt_server"."name" IS '名称';
COMMENT ON COLUMN "public"."mnt_server"."password" IS '密码';
COMMENT ON COLUMN "public"."mnt_server"."port" IS '端口';
COMMENT ON COLUMN "public"."mnt_server"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."mnt_server"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."mnt_server"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mnt_server"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."mnt_server" IS '服务器管理';

-- ----------------------------
-- Records of mnt_server
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dept";
CREATE TABLE "public"."sys_dept" (
  "dept_id" int8 NOT NULL,
  "pid" int8,
  "sub_count" int4,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "dept_sort" int4,
  "enabled" bool NOT NULL,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_dept"."dept_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_dept"."pid" IS '上级部门';
COMMENT ON COLUMN "public"."sys_dept"."sub_count" IS '子部门数目';
COMMENT ON COLUMN "public"."sys_dept"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_dept"."dept_sort" IS '排序';
COMMENT ON COLUMN "public"."sys_dept"."enabled" IS '状态';
COMMENT ON COLUMN "public"."sys_dept"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dept"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dept"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_dept"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_dept" IS '部门';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO "public"."sys_dept" VALUES (2, 7, 1, '研发部', 3, 't', 'admin', 'admin', '2019-03-25 09:15:32', '2020-08-02 14:48:47');
INSERT INTO "public"."sys_dept" VALUES (5, 7, 0, '运维部', 4, 't', 'admin', 'admin', '2019-03-25 09:20:44', '2020-05-17 14:27:27');
INSERT INTO "public"."sys_dept" VALUES (6, 8, 0, '测试部', 6, 't', 'admin', 'admin', '2019-03-25 09:52:18', '2020-06-08 11:59:21');
INSERT INTO "public"."sys_dept" VALUES (7, NULL, 2, '华南分部', 0, 't', 'admin', 'admin', '2019-03-25 11:04:50', '2020-06-08 12:08:56');
INSERT INTO "public"."sys_dept" VALUES (8, NULL, 2, '华北分部', 1, 't', 'admin', 'admin', '2019-03-25 11:04:53', '2020-05-14 12:54:00');
INSERT INTO "public"."sys_dept" VALUES (15, 8, 0, 'UI部门', 7, 't', 'admin', 'admin', '2020-05-13 22:56:53', '2020-05-14 12:54:13');
INSERT INTO "public"."sys_dept" VALUES (17, 2, 0, '研发一组', 999, 't', 'admin', 'admin', '2020-08-02 14:49:07', '2020-08-02 14:49:07');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict";
CREATE TABLE "public"."sys_dict" (
  "dict_id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_dict"."dict_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_dict"."name" IS '字典名称';
COMMENT ON COLUMN "public"."sys_dict"."description" IS '描述';
COMMENT ON COLUMN "public"."sys_dict"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dict"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dict"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_dict"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_dict" IS '数据字典';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO "public"."sys_dict" VALUES (1, 'user_status', '用户状态', NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO "public"."sys_dict" VALUES (4, 'dept_status', '部门状态', NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO "public"."sys_dict" VALUES (5, 'job_status', '岗位状态', NULL, NULL, '2019-10-27 20:31:36', NULL);

-- ----------------------------
-- Table structure for sys_dict_detail
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict_detail";
CREATE TABLE "public"."sys_dict_detail" (
  "detail_id" int8 NOT NULL,
  "dict_id" int8,
  "label" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "value" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "dict_sort" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_dict_detail"."detail_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_dict_detail"."dict_id" IS '字典id';
COMMENT ON COLUMN "public"."sys_dict_detail"."label" IS '字典标签';
COMMENT ON COLUMN "public"."sys_dict_detail"."value" IS '字典值';
COMMENT ON COLUMN "public"."sys_dict_detail"."dict_sort" IS '排序';
COMMENT ON COLUMN "public"."sys_dict_detail"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dict_detail"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dict_detail"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_dict_detail"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_dict_detail" IS '数据字典详情';

-- ----------------------------
-- Records of sys_dict_detail
-- ----------------------------
INSERT INTO "public"."sys_dict_detail" VALUES (1, 1, '激活', 'true', 1, NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO "public"."sys_dict_detail" VALUES (2, 1, '禁用', 'false', 2, NULL, NULL, NULL, NULL);
INSERT INTO "public"."sys_dict_detail" VALUES (3, 4, '启用', 'true', 1, NULL, NULL, NULL, NULL);
INSERT INTO "public"."sys_dict_detail" VALUES (4, 4, '停用', 'false', 2, NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO "public"."sys_dict_detail" VALUES (5, 5, '启用', 'true', 1, NULL, NULL, NULL, NULL);
INSERT INTO "public"."sys_dict_detail" VALUES (6, 5, '停用', 'false', 2, NULL, NULL, '2019-10-27 20:31:36', NULL);

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job";
CREATE TABLE "public"."sys_job" (
  "job_id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "enabled" bool NOT NULL,
  "job_sort" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_job"."job_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_job"."name" IS '岗位名称';
COMMENT ON COLUMN "public"."sys_job"."enabled" IS '岗位状态';
COMMENT ON COLUMN "public"."sys_job"."job_sort" IS '排序';
COMMENT ON COLUMN "public"."sys_job"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_job"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_job"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_job"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_job" IS '岗位';

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO "public"."sys_job" VALUES (8, '人事专员', 't', 3, NULL, NULL, '2019-03-29 14:52:28', NULL);
INSERT INTO "public"."sys_job" VALUES (10, '产品经理', 't', 4, NULL, NULL, '2019-03-29 14:55:51', NULL);
INSERT INTO "public"."sys_job" VALUES (11, '全栈开发', 't', 2, NULL, 'admin', '2019-03-31 13:39:30', '2020-05-05 11:33:43');
INSERT INTO "public"."sys_job" VALUES (12, '软件测试', 't', 5, NULL, 'admin', '2019-03-31 13:39:43', '2020-05-10 19:56:26');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_log";
CREATE TABLE "public"."sys_log" (
  "log_id" int8 NOT NULL,
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "log_type" varchar(255) COLLATE "pg_catalog"."default",
  "method" varchar(255) COLLATE "pg_catalog"."default",
  "params" text COLLATE "pg_catalog"."default",
  "request_ip" varchar(255) COLLATE "pg_catalog"."default",
  "time" int8,
  "username" varchar(255) COLLATE "pg_catalog"."default",
  "address" varchar(255) COLLATE "pg_catalog"."default",
  "browser" varchar(255) COLLATE "pg_catalog"."default",
  "exception_detail" text COLLATE "pg_catalog"."default",
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_log"."log_id" IS 'ID';
COMMENT ON TABLE "public"."sys_log" IS '系统日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_menu";
CREATE TABLE "public"."sys_menu" (
  "menu_id" int8 NOT NULL,
  "pid" int8,
  "sub_count" int4,
  "type" int4,
  "title" varchar(255) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "component" varchar(255) COLLATE "pg_catalog"."default",
  "menu_sort" int4,
  "icon" varchar(255) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "i_frame" varchar(1) COLLATE "pg_catalog"."default",
  "cache" varchar(1) COLLATE "pg_catalog"."default",
  "hidden" varchar(1) COLLATE "pg_catalog"."default",
  "permission" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_menu"."menu_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_menu"."pid" IS '上级菜单ID';
COMMENT ON COLUMN "public"."sys_menu"."sub_count" IS '子菜单数目';
COMMENT ON COLUMN "public"."sys_menu"."type" IS '菜单类型';
COMMENT ON COLUMN "public"."sys_menu"."title" IS '菜单标题';
COMMENT ON COLUMN "public"."sys_menu"."name" IS '组件名称';
COMMENT ON COLUMN "public"."sys_menu"."component" IS '组件';
COMMENT ON COLUMN "public"."sys_menu"."menu_sort" IS '排序';
COMMENT ON COLUMN "public"."sys_menu"."icon" IS '图标';
COMMENT ON COLUMN "public"."sys_menu"."path" IS '链接地址';
COMMENT ON COLUMN "public"."sys_menu"."i_frame" IS '是否外链';
COMMENT ON COLUMN "public"."sys_menu"."cache" IS '缓存';
COMMENT ON COLUMN "public"."sys_menu"."hidden" IS '隐藏';
COMMENT ON COLUMN "public"."sys_menu"."permission" IS '权限';
COMMENT ON COLUMN "public"."sys_menu"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_menu"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_menu"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_menu"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_menu" IS '系统菜单';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "public"."sys_menu" VALUES (1, NULL, 7, 0, '系统管理', NULL, NULL, 1, 'system', 'system', '0', '0', '0', NULL, NULL, NULL, '2018-12-18 15:11:29', NULL);
INSERT INTO "public"."sys_menu" VALUES (2, 1, 3, 1, '用户管理', 'User', 'system/user', 2, 'peoples', 'user', '0', '0', '0', 'user:list', NULL, NULL, '2018-12-18 15:14:44', NULL);
INSERT INTO "public"."sys_menu" VALUES (3, 1, 3, 1, '角色管理', 'Role', 'system/Role', 3, 'role', 'role', '0', '0', '0', 'roles:list', NULL, 'admin', '2018-12-18 15:16:07', '2021-08-31 17:22:07');
INSERT INTO "public"."sys_menu" VALUES (5, 1, 3, 1, '菜单管理', 'Menu', 'system/Menu', 5, 'menu', 'menu', '0', '0', '0', 'menu:list', NULL, NULL, '2018-12-18 15:17:28', NULL);
INSERT INTO "public"."sys_menu" VALUES (6, NULL, 5, 0, '系统监控', NULL, NULL, 10, 'monitor', 'monitor', '0', '0', '0', NULL, NULL, NULL, '2018-12-18 15:17:48', NULL);
INSERT INTO "public"."sys_menu" VALUES (7, 6, 0, 1, '操作日志', 'Log', 'monitor/log/Log', 11, 'log', 'logs', '0', '1', '0', NULL, NULL, 'admin', '2018-12-18 15:18:26', '2021-08-31 17:35:21');
INSERT INTO "public"."sys_menu" VALUES (9, 6, 0, 1, 'SQL监控', 'Sql', 'monitor/Sql', 18, 'sqlMonitor', 'druid', '0', '0', '0', NULL, NULL, 'admin', '2018-12-18 15:19:34', '2021-08-31 17:34:26');
INSERT INTO "public"."sys_menu" VALUES (10, NULL, 5, 0, '组件管理', NULL, NULL, 50, 'zujian', 'components', '0', '0', '0', NULL, NULL, NULL, '2018-12-19 13:38:16', NULL);
INSERT INTO "public"."sys_menu" VALUES (11, 10, 0, 1, '图标库', 'Icons', 'components/icons/index', 51, 'icon', 'icon', '0', '0', '0', NULL, NULL, NULL, '2018-12-19 13:38:49', NULL);
INSERT INTO "public"."sys_menu" VALUES (14, 36, 0, 1, '邮件工具', 'Email', 'tools/email/index', 35, 'email', 'email', '0', '0', '0', NULL, NULL, NULL, '2018-12-27 10:13:09', NULL);
INSERT INTO "public"."sys_menu" VALUES (15, 10, 0, 1, '富文本', 'Editor', 'components/Editor', 52, 'fwb', 'tinymce', '0', '0', '0', NULL, NULL, NULL, '2018-12-27 11:58:25', NULL);
INSERT INTO "public"."sys_menu" VALUES (18, 36, 3, 1, '存储管理', 'Storage', 'tools/storage/index', 34, 'qiniu', 'storage', '0', '0', '0', 'storage:list', NULL, NULL, '2018-12-31 11:12:15', NULL);
INSERT INTO "public"."sys_menu" VALUES (19, 36, 0, 1, '支付宝工具', 'AliPay', 'tools/aliPay/index', 37, 'alipay', 'aliPay', '0', '0', '0', NULL, NULL, NULL, '2018-12-31 14:52:38', NULL);
INSERT INTO "public"."sys_menu" VALUES (21, NULL, 2, 0, '多级菜单', NULL, '', 900, 'menu', 'nested', '0', '0', '0', NULL, NULL, 'admin', '2019-01-04 16:22:03', '2020-06-21 17:27:35');
INSERT INTO "public"."sys_menu" VALUES (22, 21, 2, 0, '二级菜单1', NULL, '', 999, 'menu', 'menu1', '0', '0', '0', NULL, NULL, 'admin', '2019-01-04 16:23:29', '2020-06-21 17:27:20');
INSERT INTO "public"."sys_menu" VALUES (23, 21, 0, 1, '二级菜单2', NULL, 'nested/menu2/index', 999, 'menu', 'menu2', '0', '0', '0', NULL, NULL, NULL, '2019-01-04 16:23:57', NULL);
INSERT INTO "public"."sys_menu" VALUES (24, 22, 0, 1, '三级菜单1', 'Test', 'nested/menu1/menu1-1', 999, 'menu', 'menu1-1', '0', '0', '0', NULL, NULL, NULL, '2019-01-04 16:24:48', NULL);
INSERT INTO "public"."sys_menu" VALUES (27, 22, 0, 1, '三级菜单2', NULL, 'nested/menu1/menu1-2', 999, 'menu', 'menu1-2', '0', '0', '0', NULL, NULL, NULL, '2019-01-07 17:27:32', NULL);
INSERT INTO "public"."sys_menu" VALUES (28, 1, 3, 1, '任务调度', 'Timing', 'system/timing/Timing', 999, 'timing', 'timing', '0', '0', '0', 'timing:list', NULL, 'admin', '2019-01-07 20:34:40', '2021-08-31 17:33:28');
INSERT INTO "public"."sys_menu" VALUES (30, 36, 0, 1, '代码生成', 'GeneratorIndex', 'generator/index', 32, 'dev', 'generator', '0', '1', '0', NULL, NULL, NULL, '2019-01-11 15:45:55', NULL);
INSERT INTO "public"."sys_menu" VALUES (32, 6, 0, 1, '异常日志', 'ErrorLog', 'monitor/log/errorLog', 12, 'error', 'errorLog', '0', '0', '0', NULL, NULL, NULL, '2019-01-13 13:49:03', NULL);
INSERT INTO "public"."sys_menu" VALUES (33, 10, 0, 1, 'Markdown', 'Markdown', 'components/MarkDown', 53, 'markdown', 'markdown', '0', '0', '0', NULL, NULL, NULL, '2019-03-08 13:46:44', NULL);
INSERT INTO "public"."sys_menu" VALUES (34, 10, 0, 1, 'Yaml编辑器', 'YamlEdit', 'components/YamlEdit', 54, 'dev', 'yaml', '0', '0', '0', NULL, NULL, NULL, '2019-03-08 15:49:40', NULL);
INSERT INTO "public"."sys_menu" VALUES (35, 1, 3, 1, '部门管理', 'Dept', 'system/dept/Dept', 6, 'dept', 'dept', '0', '0', '0', 'dept:list', NULL, 'admin', '2019-03-25 09:46:00', '2022-02-14 23:08:52');
INSERT INTO "public"."sys_menu" VALUES (36, NULL, 7, 0, '系统工具', NULL, '', 30, 'sys-tools', 'sys-tools', '0', '0', '0', NULL, NULL, NULL, '2019-03-29 10:57:35', NULL);
INSERT INTO "public"."sys_menu" VALUES (37, 1, 3, 1, '岗位管理', 'Job', 'system/job/Job', 7, 'Steve-Jobs', 'job', '0', '0', '0', 'job:list', NULL, 'admin', '2019-03-29 13:51:18', '2021-08-31 17:29:56');
INSERT INTO "public"."sys_menu" VALUES (38, 36, 0, 1, '接口文档', 'Swagger', 'tools/swagger/index', 36, 'swagger', 'swagger2', '0', '0', '0', NULL, NULL, NULL, '2019-03-29 19:57:53', NULL);
INSERT INTO "public"."sys_menu" VALUES (39, 1, 3, 1, '字典管理', 'Dict', 'system/dict/Dict', 8, 'dictionary', 'dict', '0', '0', '0', 'dict:list', NULL, 'admin', '2019-04-10 11:49:04', '2021-08-31 17:31:47');
INSERT INTO "public"."sys_menu" VALUES (41, 6, 0, 1, '在线用户', 'OnlineUser', 'monitor/Online', 10, 'Steve-Jobs', 'online', '0', '0', '0', NULL, NULL, 'admin', '2019-10-26 22:08:43', '2021-08-31 17:33:47');
INSERT INTO "public"."sys_menu" VALUES (44, 2, 0, 2, '用户新增', NULL, '', 2, '', '', '0', '0', '0', 'user:add', NULL, NULL, '2019-10-29 10:59:46', NULL);
INSERT INTO "public"."sys_menu" VALUES (45, 2, 0, 2, '用户编辑', NULL, '', 3, '', '', '0', '0', '0', 'user:edit', NULL, NULL, '2019-10-29 11:00:08', NULL);
INSERT INTO "public"."sys_menu" VALUES (46, 2, 0, 2, '用户删除', NULL, '', 4, '', '', '0', '0', '0', 'user:del', NULL, NULL, '2019-10-29 11:00:23', NULL);
INSERT INTO "public"."sys_menu" VALUES (48, 3, 0, 2, '角色创建', NULL, '', 2, '', '', '0', '0', '0', 'roles:add', NULL, NULL, '2019-10-29 12:45:34', NULL);
INSERT INTO "public"."sys_menu" VALUES (49, 3, 0, 2, '角色修改', NULL, '', 3, '', '', '0', '0', '0', 'roles:edit', NULL, NULL, '2019-10-29 12:46:16', NULL);
INSERT INTO "public"."sys_menu" VALUES (50, 3, 0, 2, '角色删除', NULL, '', 4, '', '', '0', '0', '0', 'roles:del', NULL, NULL, '2019-10-29 12:46:51', NULL);
INSERT INTO "public"."sys_menu" VALUES (52, 5, 0, 2, '菜单新增', NULL, '', 2, '', '', '0', '0', '0', 'menu:add', NULL, NULL, '2019-10-29 12:55:07', NULL);
INSERT INTO "public"."sys_menu" VALUES (53, 5, 0, 2, '菜单编辑', NULL, '', 3, '', '', '0', '0', '0', 'menu:edit', NULL, NULL, '2019-10-29 12:55:40', NULL);
INSERT INTO "public"."sys_menu" VALUES (54, 5, 0, 2, '菜单删除', NULL, '', 4, '', '', '0', '0', '0', 'menu:del', NULL, NULL, '2019-10-29 12:56:00', NULL);
INSERT INTO "public"."sys_menu" VALUES (56, 35, 0, 2, '部门新增', NULL, '', 2, '', '', '0', '0', '0', 'dept:add', NULL, NULL, '2019-10-29 12:57:09', NULL);
INSERT INTO "public"."sys_menu" VALUES (57, 35, 0, 2, '部门编辑', NULL, '', 3, '', '', '0', '0', '0', 'dept:edit', NULL, NULL, '2019-10-29 12:57:27', NULL);
INSERT INTO "public"."sys_menu" VALUES (58, 35, 0, 2, '部门删除', NULL, '', 4, '', '', '0', '0', '0', 'dept:del', NULL, NULL, '2019-10-29 12:57:41', NULL);
INSERT INTO "public"."sys_menu" VALUES (60, 37, 0, 2, '岗位新增', NULL, '', 2, '', '', '0', '0', '0', 'job:add', NULL, NULL, '2019-10-29 12:58:27', NULL);
INSERT INTO "public"."sys_menu" VALUES (61, 37, 0, 2, '岗位编辑', NULL, '', 3, '', '', '0', '0', '0', 'job:edit', NULL, NULL, '2019-10-29 12:58:45', NULL);
INSERT INTO "public"."sys_menu" VALUES (62, 37, 0, 2, '岗位删除', NULL, '', 4, '', '', '0', '0', '0', 'job:del', NULL, NULL, '2019-10-29 12:59:04', NULL);
INSERT INTO "public"."sys_menu" VALUES (64, 39, 0, 2, '字典新增', NULL, '', 2, '', '', '0', '0', '0', 'dict:add', NULL, NULL, '2019-10-29 13:00:17', NULL);
INSERT INTO "public"."sys_menu" VALUES (65, 39, 0, 2, '字典编辑', NULL, '', 3, '', '', '0', '0', '0', 'dict:edit', NULL, NULL, '2019-10-29 13:00:42', NULL);
INSERT INTO "public"."sys_menu" VALUES (66, 39, 0, 2, '字典删除', NULL, '', 4, '', '', '0', '0', '0', 'dict:del', NULL, NULL, '2019-10-29 13:00:59', NULL);
INSERT INTO "public"."sys_menu" VALUES (73, 28, 0, 2, '任务新增', NULL, '', 2, '', '', '0', '0', '0', 'timing:add', NULL, NULL, '2019-10-29 13:07:28', NULL);
INSERT INTO "public"."sys_menu" VALUES (74, 28, 0, 2, '任务编辑', NULL, '', 3, '', '', '0', '0', '0', 'timing:edit', NULL, NULL, '2019-10-29 13:07:41', NULL);
INSERT INTO "public"."sys_menu" VALUES (75, 28, 0, 2, '任务删除', NULL, '', 4, '', '', '0', '0', '0', 'timing:del', NULL, NULL, '2019-10-29 13:07:54', NULL);
INSERT INTO "public"."sys_menu" VALUES (77, 18, 0, 2, '上传文件', NULL, '', 2, '', '', '0', '0', '0', 'storage:add', NULL, NULL, '2019-10-29 13:09:09', NULL);
INSERT INTO "public"."sys_menu" VALUES (78, 18, 0, 2, '文件编辑', NULL, '', 3, '', '', '0', '0', '0', 'storage:edit', NULL, NULL, '2019-10-29 13:09:22', NULL);
INSERT INTO "public"."sys_menu" VALUES (79, 18, 0, 2, '文件删除', NULL, '', 4, '', '', '0', '0', '0', 'storage:del', NULL, NULL, '2019-10-29 13:09:34', NULL);
INSERT INTO "public"."sys_menu" VALUES (80, 6, 0, 1, '服务监控', 'ServerMonitor', 'monitor/Server', 14, 'codeConsole', 'server', '0', '0', '0', 'monitor:list', NULL, 'admin', '2019-11-07 13:06:39', '2021-08-31 17:34:04');
INSERT INTO "public"."sys_menu" VALUES (82, 36, 0, 1, '生成配置', 'GeneratorConfig', 'generator/config', 33, 'dev', 'generator/config/:tableName', '0', '1', '1', '', NULL, NULL, '2019-11-17 20:08:56', NULL);
INSERT INTO "public"."sys_menu" VALUES (83, 10, 0, 1, '图表库', 'Echarts', 'components/Echarts', 50, 'chart', 'echarts', '0', '1', '0', '', NULL, NULL, '2019-11-21 09:04:32', NULL);
INSERT INTO "public"."sys_menu" VALUES (90, NULL, 5, 1, '运维管理', 'Mnt', '', 20, 'mnt', 'mnt', '0', '0', '0', NULL, NULL, NULL, '2019-11-09 10:31:08', NULL);
INSERT INTO "public"."sys_menu" VALUES (92, 90, 3, 1, '服务器', 'ServerDeploy', 'mnt/MntServer', 22, 'server', 'mnt/serverDeploy', '0', '0', '0', 'serverDeploy:list', NULL, 'admin', '2019-11-10 10:29:25', '2021-08-31 17:36:37');
INSERT INTO "public"."sys_menu" VALUES (93, 90, 3, 1, '应用管理', 'App', 'mnt/AppList', 23, 'app', 'mnt/app', '0', '0', '0', 'app:list', NULL, 'admin', '2019-11-10 11:05:16', '2021-08-31 17:37:27');
INSERT INTO "public"."sys_menu" VALUES (94, 90, 3, 1, '部署管理', 'Deploy', 'mnt/deploy/index', 24, 'deploy', 'mnt/deploy', '0', '0', '0', 'deploy:list', NULL, NULL, '2019-11-10 15:56:55', NULL);
INSERT INTO "public"."sys_menu" VALUES (97, 90, 1, 1, '部署备份', 'DeployHistory', 'mnt/deployHistory/index', 25, 'backup', 'mnt/deployHistory', '0', '0', '0', 'deployHistory:list', NULL, NULL, '2019-11-10 16:49:44', NULL);
INSERT INTO "public"."sys_menu" VALUES (98, 90, 3, 1, '数据库管理', 'Database', 'mnt/database/index', 26, 'database', 'mnt/database', '0', '0', '0', 'database:list', NULL, NULL, '2019-11-10 20:40:04', NULL);
INSERT INTO "public"."sys_menu" VALUES (102, 97, 0, 2, '删除', NULL, '', 999, '', '', '0', '0', '0', 'deployHistory:del', NULL, NULL, '2019-11-17 09:32:48', NULL);
INSERT INTO "public"."sys_menu" VALUES (103, 92, 0, 2, '服务器新增', NULL, '', 999, '', '', '0', '0', '0', 'serverDeploy:add', NULL, NULL, '2019-11-17 11:08:33', NULL);
INSERT INTO "public"."sys_menu" VALUES (104, 92, 0, 2, '服务器编辑', NULL, '', 999, '', '', '0', '0', '0', 'serverDeploy:edit', NULL, NULL, '2019-11-17 11:08:57', NULL);
INSERT INTO "public"."sys_menu" VALUES (105, 92, 0, 2, '服务器删除', NULL, '', 999, '', '', '0', '0', '0', 'serverDeploy:del', NULL, NULL, '2019-11-17 11:09:15', NULL);
INSERT INTO "public"."sys_menu" VALUES (106, 93, 0, 2, '应用新增', NULL, '', 999, '', '', '0', '0', '0', 'app:add', NULL, NULL, '2019-11-17 11:10:03', NULL);
INSERT INTO "public"."sys_menu" VALUES (107, 93, 0, 2, '应用编辑', NULL, '', 999, '', '', '0', '0', '0', 'app:edit', NULL, NULL, '2019-11-17 11:10:28', NULL);
INSERT INTO "public"."sys_menu" VALUES (108, 93, 0, 2, '应用删除', NULL, '', 999, '', '', '0', '0', '0', 'app:del', NULL, NULL, '2019-11-17 11:10:55', NULL);
INSERT INTO "public"."sys_menu" VALUES (109, 94, 0, 2, '部署新增', NULL, '', 999, '', '', '0', '0', '0', 'deploy:add', NULL, NULL, '2019-11-17 11:11:22', NULL);
INSERT INTO "public"."sys_menu" VALUES (110, 94, 0, 2, '部署编辑', NULL, '', 999, '', '', '0', '0', '0', 'deploy:edit', NULL, NULL, '2019-11-17 11:11:41', NULL);
INSERT INTO "public"."sys_menu" VALUES (111, 94, 0, 2, '部署删除', NULL, '', 999, '', '', '0', '0', '0', 'deploy:del', NULL, NULL, '2019-11-17 11:12:01', NULL);
INSERT INTO "public"."sys_menu" VALUES (112, 98, 0, 2, '数据库新增', NULL, '', 999, '', '', '0', '0', '0', 'database:add', NULL, NULL, '2019-11-17 11:12:43', NULL);
INSERT INTO "public"."sys_menu" VALUES (113, 98, 0, 2, '数据库编辑', NULL, '', 999, '', '', '0', '0', '0', 'database:edit', NULL, NULL, '2019-11-17 11:12:58', NULL);
INSERT INTO "public"."sys_menu" VALUES (114, 98, 0, 2, '数据库删除', NULL, '', 999, '', '', '0', '0', '0', 'database:del', NULL, NULL, '2019-11-17 11:13:14', NULL);
INSERT INTO "public"."sys_menu" VALUES (116, 36, 0, 1, '生成预览', 'Preview', 'generator/preview', 999, 'java', 'generator/preview/:tableName', '0', '1', '1', NULL, NULL, NULL, '2019-11-26 14:54:36', NULL);

-- ----------------------------
-- Table structure for sys_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_quartz_job";
CREATE TABLE "public"."sys_quartz_job" (
  "job_id" int8 NOT NULL,
  "bean_name" varchar(255) COLLATE "pg_catalog"."default",
  "cron_expression" varchar(255) COLLATE "pg_catalog"."default",
  "is_pause" varchar(1) COLLATE "pg_catalog"."default",
  "job_name" varchar(255) COLLATE "pg_catalog"."default",
  "method_name" varchar(255) COLLATE "pg_catalog"."default",
  "params" varchar(255) COLLATE "pg_catalog"."default",
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "person_in_charge" varchar(100) COLLATE "pg_catalog"."default",
  "email" varchar(100) COLLATE "pg_catalog"."default",
  "sub_task" varchar(100) COLLATE "pg_catalog"."default",
  "pause_after_failure" varchar(1) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_quartz_job"."job_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_quartz_job"."bean_name" IS 'Spring Bean名称';
COMMENT ON COLUMN "public"."sys_quartz_job"."cron_expression" IS 'cron 表达式';
COMMENT ON COLUMN "public"."sys_quartz_job"."is_pause" IS '状态：1暂停、0启用';
COMMENT ON COLUMN "public"."sys_quartz_job"."job_name" IS '任务名称';
COMMENT ON COLUMN "public"."sys_quartz_job"."method_name" IS '方法名称';
COMMENT ON COLUMN "public"."sys_quartz_job"."params" IS '参数';
COMMENT ON COLUMN "public"."sys_quartz_job"."description" IS '备注';
COMMENT ON COLUMN "public"."sys_quartz_job"."person_in_charge" IS '负责人';
COMMENT ON COLUMN "public"."sys_quartz_job"."email" IS '报警邮箱';
COMMENT ON COLUMN "public"."sys_quartz_job"."sub_task" IS '子任务ID';
COMMENT ON COLUMN "public"."sys_quartz_job"."pause_after_failure" IS '任务失败后是否暂停';
COMMENT ON COLUMN "public"."sys_quartz_job"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_quartz_job"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_quartz_job"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_quartz_job"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_quartz_job" IS '定时任务';

-- ----------------------------
-- Records of sys_quartz_job
-- ----------------------------
INSERT INTO "public"."sys_quartz_job" VALUES (2, 'testTask', '0/5 * * * * ?', '1', '测试1', 'run1', 'test', '带参测试，多参使用json', '测试', NULL, NULL, NULL, NULL, 'admin', '2019-08-22 14:08:29', '2020-05-24 13:58:33');
INSERT INTO "public"."sys_quartz_job" VALUES (3, 'testTask', '0/5 * * * * ?', '1', '测试', 'run', '', '不带参测试', 'Zheng Jie', '', '5,6', '1', NULL, 'admin', '2019-09-26 16:44:39', '2020-05-24 14:48:12');
INSERT INTO "public"."sys_quartz_job" VALUES (5, 'Test', '0/5 * * * * ?', '1', '任务告警测试', 'run', NULL, '测试', 'test', '', NULL, '1', 'admin', 'admin', '2020-05-05 20:32:41', '2020-05-05 20:36:13');
INSERT INTO "public"."sys_quartz_job" VALUES (6, 'testTask', '0/5 * * * * ?', '1', '测试3', 'run2', NULL, '测试3', 'Zheng Jie', '', NULL, '1', 'admin', 'admin', '2020-05-05 20:35:41', '2022-03-15 13:20:29');

-- ----------------------------
-- Table structure for sys_quartz_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_quartz_log";
CREATE TABLE "public"."sys_quartz_log" (
  "log_id" int8 NOT NULL,
  "bean_name" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "cron_expression" varchar(255) COLLATE "pg_catalog"."default",
  "exception_detail" text COLLATE "pg_catalog"."default",
  "is_success" varchar(1) COLLATE "pg_catalog"."default",
  "job_name" varchar(255) COLLATE "pg_catalog"."default",
  "method_name" varchar(255) COLLATE "pg_catalog"."default",
  "params" varchar(255) COLLATE "pg_catalog"."default",
  "time" int8
)
;
COMMENT ON COLUMN "public"."sys_quartz_log"."log_id" IS 'ID';
COMMENT ON TABLE "public"."sys_quartz_log" IS '定时任务日志';

-- ----------------------------
-- Records of sys_quartz_log
-- ----------------------------
INSERT INTO "public"."sys_quartz_log" VALUES (151, 'testTask', '2022-03-14 23:19:55', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 87);
INSERT INTO "public"."sys_quartz_log" VALUES (152, 'testTask', '2022-03-14 23:20:09', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (153, 'testTask', '2022-03-14 23:20:10', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (154, 'testTask', '2022-03-14 23:20:14', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (155, 'testTask', '2022-03-14 23:20:15', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (156, 'testTask', '2022-03-14 23:20:20', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (157, 'testTask', '2022-03-14 23:20:25', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (158, 'testTask', '2022-03-14 23:20:30', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (159, 'testTask', '2022-03-15 13:17:25', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 2);
INSERT INTO "public"."sys_quartz_log" VALUES (160, 'testTask', '2022-03-15 13:17:30', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (161, 'testTask', '2022-03-15 13:17:35', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (162, 'testTask', '2022-03-15 13:17:40', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (163, 'testTask', '2022-03-15 13:17:45', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (164, 'testTask', '2022-03-15 13:17:50', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (165, 'testTask', '2022-03-15 13:17:55', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (166, 'testTask', '2022-03-15 13:18:00', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (167, 'testTask', '2022-03-15 13:18:05', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (168, 'testTask', '2022-03-15 13:18:10', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (169, 'testTask', '2022-03-15 13:18:15', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (170, 'testTask', '2022-03-15 13:18:20', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (171, 'testTask', '2022-03-15 13:18:25', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (172, 'testTask', '2022-03-15 13:18:30', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (173, 'testTask', '2022-03-15 13:18:35', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (174, 'testTask', '2022-03-15 13:18:40', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (175, 'testTask', '2022-03-15 13:18:45', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (176, 'testTask', '2022-03-15 13:18:50', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (177, 'testTask', '2022-03-15 13:18:55', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (178, 'testTask', '2022-03-15 13:19:00', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (179, 'testTask', '2022-03-15 13:19:05', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (180, 'testTask', '2022-03-15 13:19:10', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (181, 'testTask', '2022-03-15 13:19:15', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (182, 'testTask', '2022-03-15 13:19:20', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (183, 'testTask', '2022-03-15 13:19:25', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (184, 'testTask', '2022-03-15 13:19:30', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (185, 'testTask', '2022-03-15 13:19:35', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (186, 'testTask', '2022-03-15 13:19:40', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (187, 'testTask', '2022-03-15 13:19:45', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (188, 'testTask', '2022-03-15 13:19:50', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (189, 'testTask', '2022-03-15 13:19:55', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (190, 'testTask', '2022-03-15 13:20:00', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 1);
INSERT INTO "public"."sys_quartz_log" VALUES (191, 'testTask', '2022-03-15 13:20:05', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (192, 'testTask', '2022-03-15 13:20:10', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (193, 'testTask', '2022-03-15 13:20:15', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (194, 'testTask', '2022-03-15 13:20:20', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);
INSERT INTO "public"."sys_quartz_log" VALUES (195, 'testTask', '2022-03-15 13:20:25', '0/5 * * * * ?', NULL, '1', '测试3', 'run2', NULL, 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role" (
  "role_id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "level" int4,
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "data_scope" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_role"."role_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_role"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_role"."level" IS '角色级别';
COMMENT ON COLUMN "public"."sys_role"."description" IS '描述';
COMMENT ON COLUMN "public"."sys_role"."data_scope" IS '数据权限';
COMMENT ON COLUMN "public"."sys_role"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_role"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_role"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_role"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_role" IS '角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "public"."sys_role" VALUES (1, '超级管理员', 1, '-', '全部', NULL, 'admin', '2018-11-23 11:04:37', '2020-08-06 16:10:24');
INSERT INTO "public"."sys_role" VALUES (2, '普通用户', 2, '-', '本级', NULL, 'admin', '2018-11-23 13:09:06', '2020-09-05 10:45:12');

-- ----------------------------
-- Table structure for sys_roles_depts
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_roles_depts";
CREATE TABLE "public"."sys_roles_depts" (
  "role_id" int8 NOT NULL,
  "dept_id" int8 NOT NULL
)
;
COMMENT ON TABLE "public"."sys_roles_depts" IS '角色部门关联';

-- ----------------------------
-- Records of sys_roles_depts
-- ----------------------------

-- ----------------------------
-- Table structure for sys_roles_menus
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_roles_menus";
CREATE TABLE "public"."sys_roles_menus" (
  "menu_id" int8 NOT NULL,
  "role_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_roles_menus"."menu_id" IS '菜单ID';
COMMENT ON COLUMN "public"."sys_roles_menus"."role_id" IS '角色ID';
COMMENT ON TABLE "public"."sys_roles_menus" IS '角色菜单关联';

-- ----------------------------
-- Records of sys_roles_menus
-- ----------------------------
INSERT INTO "public"."sys_roles_menus" VALUES (1, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (2, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (3, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (5, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (6, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (7, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (9, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (10, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (11, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (14, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (15, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (18, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (19, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (21, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (22, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (23, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (24, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (27, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (28, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (30, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (32, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (33, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (34, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (35, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (36, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (37, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (38, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (39, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (41, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (44, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (45, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (46, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (48, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (49, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (50, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (52, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (53, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (54, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (56, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (57, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (58, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (60, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (61, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (62, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (64, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (65, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (66, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (73, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (74, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (75, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (77, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (78, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (79, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (80, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (82, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (83, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (90, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (92, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (93, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (94, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (97, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (98, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (102, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (103, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (104, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (105, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (106, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (107, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (108, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (109, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (110, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (111, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (112, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (113, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (114, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (116, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (120, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (1, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (2, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (6, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (7, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (9, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (10, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (11, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (14, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (15, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (19, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (21, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (22, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (23, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (24, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (27, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (30, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (32, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (33, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (34, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (36, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (80, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (82, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (83, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (116, 2);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user" (
  "user_id" int8 NOT NULL,
  "dept_id" int8,
  "username" varchar(255) COLLATE "pg_catalog"."default",
  "nick_name" varchar(255) COLLATE "pg_catalog"."default",
  "gender" varchar(2) COLLATE "pg_catalog"."default",
  "phone" varchar(255) COLLATE "pg_catalog"."default",
  "email" varchar(255) COLLATE "pg_catalog"."default",
  "avatar_name" varchar(255) COLLATE "pg_catalog"."default",
  "avatar_path" varchar(255) COLLATE "pg_catalog"."default",
  "password" varchar(255) COLLATE "pg_catalog"."default",
  "is_admin" bool,
  "enabled" bool,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "pwd_reset_time" timestamp(6),
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_user"."user_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_user"."dept_id" IS '部门名称';
COMMENT ON COLUMN "public"."sys_user"."username" IS '用户名';
COMMENT ON COLUMN "public"."sys_user"."nick_name" IS '昵称';
COMMENT ON COLUMN "public"."sys_user"."gender" IS '性别';
COMMENT ON COLUMN "public"."sys_user"."phone" IS '手机号码';
COMMENT ON COLUMN "public"."sys_user"."email" IS '邮箱';
COMMENT ON COLUMN "public"."sys_user"."avatar_name" IS '头像地址';
COMMENT ON COLUMN "public"."sys_user"."avatar_path" IS '头像真实路径';
COMMENT ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT ON COLUMN "public"."sys_user"."is_admin" IS '是否为admin账号';
COMMENT ON COLUMN "public"."sys_user"."enabled" IS '状态：1启用、0禁用';
COMMENT ON COLUMN "public"."sys_user"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_user"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_user"."pwd_reset_time" IS '修改密码的时间';
COMMENT ON COLUMN "public"."sys_user"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_user"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_user" IS '系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user" VALUES (1, 5, 'admin', '管理员', '男', '18856958745', '201507802@qq.com', 'avatar-202201171250228.png', 'C:\eladmin\avatar\avatar-202201171250228.png', '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', 't', 't', NULL, 'admin', '2020-05-03 16:38:31', '2018-08-23 09:11:56', '2022-01-17 20:11:39');
INSERT INTO "public"."sys_user" VALUES (2, 2, 'test', '测试', '男', '19999999999', '231@qq.com', NULL, NULL, '$2a$10$4XcyudOYTSz6fue6KFNMHeUQnCX5jbBQypLEnGk1PmekXt5c95JcK', 'f', 't', 'admin', 'admin', NULL, '2020-05-05 11:15:49', '2022-06-10 22:38:19.458');

-- ----------------------------
-- Table structure for sys_users_jobs
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_users_jobs";
CREATE TABLE "public"."sys_users_jobs" (
  "user_id" int8 NOT NULL,
  "job_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_users_jobs"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_users_jobs"."job_id" IS '岗位ID';

-- ----------------------------
-- Records of sys_users_jobs
-- ----------------------------
INSERT INTO "public"."sys_users_jobs" VALUES (1, 11);
INSERT INTO "public"."sys_users_jobs" VALUES (2, 12);

-- ----------------------------
-- Table structure for sys_users_roles
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_users_roles";
CREATE TABLE "public"."sys_users_roles" (
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_users_roles"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_users_roles"."role_id" IS '角色ID';
COMMENT ON TABLE "public"."sys_users_roles" IS '用户角色关联';

-- ----------------------------
-- Records of sys_users_roles
-- ----------------------------
INSERT INTO "public"."sys_users_roles" VALUES (1, 1);
INSERT INTO "public"."sys_users_roles" VALUES (2, 2);

-- ----------------------------
-- Table structure for tool_alipay_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."tool_alipay_config";
CREATE TABLE "public"."tool_alipay_config" (
  "config_id" int8 NOT NULL,
  "app_id" varchar(255) COLLATE "pg_catalog"."default",
  "charset" varchar(255) COLLATE "pg_catalog"."default",
  "format" varchar(255) COLLATE "pg_catalog"."default",
  "gateway_url" varchar(255) COLLATE "pg_catalog"."default",
  "notify_url" varchar(255) COLLATE "pg_catalog"."default",
  "private_key" text COLLATE "pg_catalog"."default",
  "public_key" text COLLATE "pg_catalog"."default",
  "return_url" varchar(255) COLLATE "pg_catalog"."default",
  "sign_type" varchar(255) COLLATE "pg_catalog"."default",
  "sys_service_provider_id" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."tool_alipay_config"."config_id" IS 'ID';
COMMENT ON COLUMN "public"."tool_alipay_config"."app_id" IS '应用ID';
COMMENT ON COLUMN "public"."tool_alipay_config"."charset" IS '编码';
COMMENT ON COLUMN "public"."tool_alipay_config"."format" IS '类型 固定格式json';
COMMENT ON COLUMN "public"."tool_alipay_config"."gateway_url" IS '网关地址';
COMMENT ON COLUMN "public"."tool_alipay_config"."notify_url" IS '异步回调';
COMMENT ON COLUMN "public"."tool_alipay_config"."private_key" IS '私钥';
COMMENT ON COLUMN "public"."tool_alipay_config"."public_key" IS '公钥';
COMMENT ON COLUMN "public"."tool_alipay_config"."return_url" IS '回调地址';
COMMENT ON COLUMN "public"."tool_alipay_config"."sign_type" IS '签名方式';
COMMENT ON COLUMN "public"."tool_alipay_config"."sys_service_provider_id" IS '商户号';
COMMENT ON TABLE "public"."tool_alipay_config" IS '支付宝配置类';

-- ----------------------------
-- Records of tool_alipay_config
-- ----------------------------
INSERT INTO "public"."tool_alipay_config" VALUES (1, '2016091700532697', 'utf-8', 'JSON', 'https://openapi.alipaydev.com/gateway.do', 'http://api.auauz.net/api/aliPay/notify', 'MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC5js8sInU10AJ0cAQ8UMMyXrQ+oHZEkVt5lBwsStmTJ7YikVYgbskx1YYEXTojRsWCb+SH/kDmDU4pK/u91SJ4KFCRMF2411piYuXU/jF96zKrADznYh/zAraqT6hvAIVtQAlMHN53nx16rLzZ/8jDEkaSwT7+HvHiS+7sxSojnu/3oV7BtgISoUNstmSe8WpWHOaWv19xyS+Mce9MY4BfseFhzTICUymUQdd/8hXA28/H6osUfAgsnxAKv7Wil3aJSgaJczWuflYOve0dJ3InZkhw5Cvr0atwpk8YKBQjy5CdkoHqvkOcIB+cYHXJKzOE5tqU7inSwVbHzOLQ3XbnAgMBAAECggEAVJp5eT0Ixg1eYSqFs9568WdetUNCSUchNxDBu6wxAbhUgfRUGZuJnnAll63OCTGGck+EGkFh48JjRcBpGoeoHLL88QXlZZbC/iLrea6gcDIhuvfzzOffe1RcZtDFEj9hlotg8dQj1tS0gy9pN9g4+EBH7zeu+fyv+qb2e/v1l6FkISXUjpkD7RLQr3ykjiiEw9BpeKb7j5s7Kdx1NNIzhkcQKNqlk8JrTGDNInbDM6inZfwwIO2R1DHinwdfKWkvOTODTYa2MoAvVMFT9Bec9FbLpoWp7ogv1JMV9svgrcF9XLzANZ/OQvkbe9TV9GWYvIbxN6qwQioKCWO4GPnCAQKBgQDgW5MgfhX8yjXqoaUy/d1VjI8dHeIyw8d+OBAYwaxRSlCfyQ+tieWcR2HdTzPca0T0GkWcKZm0ei5xRURgxt4DUDLXNh26HG0qObbtLJdu/AuBUuCqgOiLqJ2f1uIbrz6OZUHns+bT/jGW2Ws8+C13zTCZkZt9CaQsrp3QOGDx5wKBgQDTul39hp3ZPwGNFeZdkGoUoViOSd5Lhowd5wYMGAEXWRLlU8z+smT5v0POz9JnIbCRchIY2FAPKRdVTICzmPk2EPJFxYTcwaNbVqL6lN7J2IlXXMiit5QbiLauo55w7plwV6LQmKm9KV7JsZs5XwqF7CEovI7GevFzyD3w+uizAQKBgC3LY1eRhOlpWOIAhpjG6qOoohmeXOphvdmMlfSHq6WYFqbWwmV4rS5d/6LNpNdL6fItXqIGd8I34jzql49taCmi+A2nlR/E559j0mvM20gjGDIYeZUz5MOE8k+K6/IcrhcgofgqZ2ZED1ksHdB/E8DNWCswZl16V1FrfvjeWSNnAoGAMrBplCrIW5xz+J0Hm9rZKrs+AkK5D4fUv8vxbK/KgxZ2KaUYbNm0xv39c+PZUYuFRCz1HDGdaSPDTE6WeWjkMQd5mS6ikl9hhpqFRkyh0d0fdGToO9yLftQKOGE/q3XUEktI1XvXF0xyPwNgUCnq0QkpHyGVZPtGFxwXiDvpvgECgYA5PoB+nY8iDiRaJNko9w0hL4AeKogwf+4TbCw+KWVEn6jhuJa4LFTdSqp89PktQaoVpwv92el/AhYjWOl/jVCm122f9b7GyoelbjMNolToDwe5pF5RnSpEuDdLy9MfE8LnE3PlbE7E5BipQ3UjSebkgNboLHH/lNZA5qvEtvbfvQ==', 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAut9evKRuHJ/2QNfDlLwvN/S8l9hRAgPbb0u61bm4AtzaTGsLeMtScetxTWJnVvAVpMS9luhEJjt+Sbk5TNLArsgzzwARgaTKOLMT1TvWAK5EbHyI+eSrc3s7Awe1VYGwcubRFWDm16eQLv0k7iqiw+4mweHSz/wWyvBJVgwLoQ02btVtAQErCfSJCOmt0Q/oJQjj08YNRV4EKzB19+f5A+HQVAKy72dSybTzAK+3FPtTtNen/+b5wGeat7c32dhYHnGorPkPeXLtsqqUTp1su5fMfd4lElNdZaoCI7osZxWWUo17vBCZnyeXc9fk0qwD9mK6yRAxNbrY72Xx5VqIqwIDAQAB', 'http://api.auauz.net/api/aliPay/return', 'RSA2', '2088102176044281');

-- ----------------------------
-- Table structure for tool_email_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."tool_email_config";
CREATE TABLE "public"."tool_email_config" (
  "config_id" int8 NOT NULL,
  "from_user" varchar(255) COLLATE "pg_catalog"."default",
  "host" varchar(255) COLLATE "pg_catalog"."default",
  "pass" varchar(255) COLLATE "pg_catalog"."default",
  "port" varchar(255) COLLATE "pg_catalog"."default",
  "user" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."tool_email_config"."config_id" IS 'ID';
COMMENT ON COLUMN "public"."tool_email_config"."from_user" IS '收件人';
COMMENT ON COLUMN "public"."tool_email_config"."host" IS '邮件服务器SMTP地址';
COMMENT ON COLUMN "public"."tool_email_config"."pass" IS '密码';
COMMENT ON COLUMN "public"."tool_email_config"."port" IS '端口';
COMMENT ON COLUMN "public"."tool_email_config"."user" IS '发件者用户名';
COMMENT ON TABLE "public"."tool_email_config" IS '邮箱配置';

-- ----------------------------
-- Records of tool_email_config
-- ----------------------------

-- ----------------------------
-- Table structure for tool_local_storage
-- ----------------------------
DROP TABLE IF EXISTS "public"."tool_local_storage";
CREATE TABLE "public"."tool_local_storage" (
  "storage_id" int8 NOT NULL,
  "real_name" varchar(255) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "suffix" varchar(255) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(255) COLLATE "pg_catalog"."default",
  "size" varchar(100) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."tool_local_storage"."storage_id" IS 'ID';
COMMENT ON COLUMN "public"."tool_local_storage"."real_name" IS '文件真实的名称';
COMMENT ON COLUMN "public"."tool_local_storage"."name" IS '文件名';
COMMENT ON COLUMN "public"."tool_local_storage"."suffix" IS '后缀';
COMMENT ON COLUMN "public"."tool_local_storage"."path" IS '路径';
COMMENT ON COLUMN "public"."tool_local_storage"."type" IS '类型';
COMMENT ON COLUMN "public"."tool_local_storage"."size" IS '大小';
COMMENT ON COLUMN "public"."tool_local_storage"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."tool_local_storage"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."tool_local_storage"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."tool_local_storage"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."tool_local_storage" IS '本地存储';

-- ----------------------------
-- Records of tool_local_storage
-- ----------------------------

-- ----------------------------
-- Table structure for tool_qiniu_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."tool_qiniu_config";
CREATE TABLE "public"."tool_qiniu_config" (
  "config_id" int8 NOT NULL,
  "access_key" text COLLATE "pg_catalog"."default",
  "bucket" varchar(255) COLLATE "pg_catalog"."default",
  "host" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "secret_key" text COLLATE "pg_catalog"."default",
  "type" varchar(255) COLLATE "pg_catalog"."default",
  "zone" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."tool_qiniu_config"."config_id" IS 'ID';
COMMENT ON COLUMN "public"."tool_qiniu_config"."access_key" IS 'accessKey';
COMMENT ON COLUMN "public"."tool_qiniu_config"."bucket" IS 'Bucket 识别符';
COMMENT ON COLUMN "public"."tool_qiniu_config"."host" IS '外链域名';
COMMENT ON COLUMN "public"."tool_qiniu_config"."secret_key" IS 'secretKey';
COMMENT ON COLUMN "public"."tool_qiniu_config"."type" IS '空间类型';
COMMENT ON COLUMN "public"."tool_qiniu_config"."zone" IS '机房';
COMMENT ON TABLE "public"."tool_qiniu_config" IS '七牛云配置';

-- ----------------------------
-- Records of tool_qiniu_config
-- ----------------------------

-- ----------------------------
-- Table structure for tool_qiniu_content
-- ----------------------------
DROP TABLE IF EXISTS "public"."tool_qiniu_content";
CREATE TABLE "public"."tool_qiniu_content" (
  "content_id" int8 NOT NULL,
  "bucket" varchar(255) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "size" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(255) COLLATE "pg_catalog"."default",
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "suffix" varchar(255) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."tool_qiniu_content"."content_id" IS 'ID';
COMMENT ON COLUMN "public"."tool_qiniu_content"."bucket" IS 'Bucket 识别符';
COMMENT ON COLUMN "public"."tool_qiniu_content"."name" IS '文件名称';
COMMENT ON COLUMN "public"."tool_qiniu_content"."size" IS '文件大小';
COMMENT ON COLUMN "public"."tool_qiniu_content"."type" IS '文件类型：私有或公开';
COMMENT ON COLUMN "public"."tool_qiniu_content"."url" IS '文件url';
COMMENT ON COLUMN "public"."tool_qiniu_content"."suffix" IS '文件后缀';
COMMENT ON COLUMN "public"."tool_qiniu_content"."update_time" IS '上传或同步的时间';
COMMENT ON TABLE "public"."tool_qiniu_content" IS '七牛云文件存储';

-- ----------------------------
-- Records of tool_qiniu_content
-- ----------------------------

-- ----------------------------
-- Indexes structure for table code_column_config
-- ----------------------------
CREATE INDEX "idx_table_name" ON "public"."code_column_config" USING btree (
  "table_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table code_column_config
-- ----------------------------
ALTER TABLE "public"."code_column_config" ADD CONSTRAINT "code_column_config_pkey" PRIMARY KEY ("column_id");

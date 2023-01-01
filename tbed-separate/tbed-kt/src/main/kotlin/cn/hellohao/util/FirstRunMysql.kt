package cn.hellohao.util

import cn.hellohao.config.GlobalConstant
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.util.*

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/11/29 14:32
 */
//@Configuration
class FirstRunMysql : InitializingBean {
    @Value("\${spring.datasource.username}")
    private val jdbcusername: String? = null

    @Value("\${spring.datasource.password}")
    private val jdbcpass: String? = null

    @Value("\${spring.datasource.url}")
    private val jdbcurl: String? = null
    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        isWindows
        RunSqlScript.USERNAME = jdbcusername
        RunSqlScript.PASSWORD = jdbcpass
        RunSqlScript.DBURL = jdbcurl
        Print.Normal("正在校验数据库参数...")
        RunSqlScript.RunInsert(dynamic)
        RunSqlScript.RunInsert(compressed)
        val uid = UUID.randomUUID().toString().replace("-", "").lowercase(Locale.getDefault())
        RunSqlScript.RunInsert("update user set uid='$uid' where id = 1")
        val ret1 = RunSqlScript.RunSelectCount(sql1)
        if (ret1 == 0) {
            Print.Normal("In execution...")
            RunSqlScript.RunInsert(sql2)
        } else {
            if (ret1!! > 0) {
                Print.Normal("Stage 1")
            } else {
                Print.Normal("Mysql 报了一个错")
            }
        }
        val imgandalbum = RunSqlScript.RunSelectCount("$judgeTable'imgandalbum'")
        if (imgandalbum == 0) {
            RunSqlScript.RunInsert(sql3)
            Print.Normal("Stage 2")
        }
        val album = RunSqlScript.RunSelectCount("$judgeTable'album'")
        if (album == 0) {
            RunSqlScript.RunInsert(sql4)
            Print.Normal("Stage 3")
        }
        val ret2 = RunSqlScript.RunSelectCount("$judgeTable'img_data' and column_name = 'explains'")
        if (ret2 == 0) {
            RunSqlScript.RunInsert(sql6)
            Print.Normal("Stage 4")
        }
        val ret3 = RunSqlScript.RunSelectCount("$judgeTable'album' and column_name = 'userid'")
        if (ret3 == 0) {
            RunSqlScript.RunInsert(sql7)
            Print.Normal("Stage 5")
        }
        val ret4 = RunSqlScript.RunSelectCount("$judgeTable'img_data' and column_name = 'md5key'")
        if (ret4 == 0) {
            RunSqlScript.RunInsert(sql8)
            Print.Normal("Stage 6")
        }
        val ret5 = RunSqlScript.RunSelectCount("$judgeTable'config' and column_name = 'theme'")
        if (ret5 == 0) {
            RunSqlScript.RunInsert(sql9)
            Print.Normal("Stage 7")
        }
        val ret6 =
            RunSqlScript.RunSelectCount("$judgeTable'imgandalbum' and column_name = 'notes' and TABLE_SCHEMA='picturebed'")
        if (ret6 == 0) {
            RunSqlScript.RunInsert(sql11)
            Print.Normal("Stage 8")
        }
        RunSqlScript.RunInsert(sql10)

        //RunSqlScript.RunInsert("alter table img_data drop index index_md5key_url");
        RunSqlScript.RunInsert(inddx_md5key)
        RunSqlScript.RunInsert("UPDATE `storage_key` SET `Endpoint` = '0' WHERE `id` = 8")
        Print.Normal("Stage success")
    }

    //检查表是否存在，后边加'img_data' and column_name = 'explains'  检查字段是否存在
    private val judgeTable = "select count(*) from information_schema.columns where table_name = "

    //创建blacklist  2019-11-29
    private val sql1 =
        "select count(*) from information_schema.columns where table_name = 'uploadconfig' and column_name = 'blacklist'"
    private val sql2 = "alter table upload_config add blacklist varchar(500);"

    //创建imgandalbum和album  添加img_data表字段explain 2019-12-20
    private val sql3 =
        "CREATE TABLE `imgandalbum`  (`imgname` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,`albumkey` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic"
    private val sql4 =
        "CREATE TABLE `album`  (`albumkey` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,`albumtitle` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,`createdate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL, `password` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic"
    private val sql6 = "alter table img_data add explains varchar(5000)"

    //添加album表字段userid
    private val sql7 = "alter table album add userid int(10)"

    //添加img_data表字段md5key
    private val sql8 = "alter table img_data add md5key varchar(5000)"
    private val sql9 = "ALTER TABLE config ADD theme int(4) DEFAULT '1' COMMENT '主题'  "

    //修改字段长度
    private val sql10 =
        "alter table config modify column `explain` varchar(1000),modify column links varchar(1000),modify column notice varchar(1000),modify column baidu varchar(1000)"

    //添加imgandalbum字段notes(添加画廊图片说明)
    private val sql11 = "alter table imgandalbum add notes varchar(1000) DEFAULT ' '"
    private val inddx_md5key = "ALTER TABLE img_data ADD INDEX index_md5key_url ( md5key,imgurl)"

    //                             create index yarn_app_result_i4 on yarn_app_result (flow_exec_id(100), another_column(50));
    private val dynamic = "alter table img_data row_format=dynamic"
    private val compressed = "alter table img_data row_format=compressed"
    private fun clears() {
        val file1 = File(GlobalConstant.LOCPATH + File.separator + "hellohaotempimg")
        val file2 = File(GlobalConstant.LOCPATH + File.separator + "hellohaotempwatermarimg")

        //判断目录有没有创建
        val file = File(GlobalConstant.LOCPATH)
        if (!file.exists()) {
            file.mkdirs()
            file1.mkdirs()
            file2.mkdirs()
        } else {
            if (!file1.exists()) {
                file1.mkdirs()
            } else if (!file2.exists()) {
                file2.mkdirs()
            }
        }
    }

    val isWindows: Boolean
        get() {
            println("当前系统类型:" + System.getProperties().getProperty("os.name").uppercase(Locale.getDefault()))
            if (System.getProperties().getProperty("os.name").uppercase(Locale.getDefault()).contains("MAC")) {
                GlobalConstant.SYSTYPE = "MAC"
                val props = System.getProperties()
                GlobalConstant.LOCPATH = props.getProperty("user.home") + File.separator + ".HellohaoData"
            }
            return System.getProperties().getProperty("os.name").uppercase(Locale.getDefault()).indexOf("WINDOWS") != -1
        }
}
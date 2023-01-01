package cn.hellohao.util

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

internal object RunSqlScript {
    var DBDRIVER = "org.postgresql.Driver"
    var DBURL: String? = null

    //现在使用的是mysql数据库，是直接连接的，所以此处必须有用户名和密码
    var USERNAME: String? = null
    var PASSWORD: String? = null
    fun RunSelectCount(sql: String?): Int {
        var count = 0
        //数据库连接对象
        var conn: Connection? = null
        //数据库操作对象
        var stmt: Statement? = null
        //1、加载驱动程序
        try {
            Class.forName(DBDRIVER)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        //2、连接数据库
        //通过连接管理器连接数据库
        try {
            //在连接的时候直接输入用户名和密码才可以连接
            conn = DriverManager.getConnection(DBURL, USERNAME, PASSWORD)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        //3、向数据库中插入一条数据
        //String sql = "select count(*) from information_schema.columns where table_name = 'config' and column_name = 'id'";
        try {
            stmt = conn!!.createStatement()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        //4、执行语句
        try {
            //stmt.executeUpdate(sql);
            val resultSet = stmt!!.executeQuery(sql)
            if (resultSet.next()) {
                count = resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            return -1
        }
        //5、关闭操作
        try {
            stmt.close()
            conn!!.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return count
    }

    //insert操作
    fun RunInsert(sql: String?): Int {
        var count = 0
        //数据库连接对象
        var conn: Connection? = null
        //数据库操作对象
        var stmt: Statement? = null
        //1、加载驱动程序
        try {
            Class.forName(DBDRIVER)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            Print.Normal("SqlHelper.java - ClassNotFoundException")
        }
        //2、连接数据库
        //通过连接管理器连接数据库
        try {
            //在连接的时候直接输入用户名和密码才可以连接
            conn = DriverManager.getConnection(DBURL, USERNAME, PASSWORD)
        } catch (e: SQLException) {
            e.printStackTrace()
            Print.Normal("SqlHelper.java - SQLException1")
        }
        try {
            stmt = conn!!.createStatement()
        } catch (e: SQLException) {
            e.printStackTrace()
            Print.Normal("SqlHelper.java - SQLException2")
        }
        //4、执行语句
        try {
            count = stmt!!.executeUpdate(sql)
        } catch (e: SQLException) {
            if (e.message!!.contains("Duplicate key name") && e.message!!.contains("index_md5key_url")) {
                Print.Normal("Database index already exists, skip this step.")
            } else {
                e.printStackTrace()
            }
        }
        //5、关闭操作
        try {
            stmt!!.close()
            conn!!.close()
        } catch (e: SQLException) {
            Print.Normal("SqlHelper.java - SQLException4")
        }
        return count
    }
}
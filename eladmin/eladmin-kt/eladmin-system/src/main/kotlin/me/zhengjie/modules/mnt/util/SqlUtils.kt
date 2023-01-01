/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.mnt.util

import com.alibaba.druid.pool.DruidDataSourceimport

com.alibaba.druid.util.StringUtilsimport com.google.common.collect.Listsimport lombok.extern.slf4j.Slf4jimport me.zhengjie.utils.CloseUtil.closeimport java.io.BufferedReaderimport java.io.Fileimport java.io.FileInputStreamimport java.io.InputStreamReaderimport java.nio.charset.StandardCharsetsimport java.sql.Connectionimport java.sql.DriverManagerimport java.sql.SQLExceptionimport java.sql.Statementimport javax.sql.DataSource
/**
 * @author /
 */
@Slf4j
object SqlUtils {
    /**
     * 获取数据源
     *
     * @param jdbcUrl /
     * @param userName /
     * @param password /
     * @return DataSource
     */
    private fun getDataSource(jdbcUrl: String, userName: String, password: String): DataSource {
        val druidDataSource = DruidDataSource()
        val className: String
        className = try {
            DriverManager.getDriver(jdbcUrl.trim { it <= ' ' }).javaClass.name
        } catch (e: SQLException) {
            throw RuntimeException("Get class name error: =$jdbcUrl")
        }
        if (StringUtils.isEmpty(className)) {
            val dataTypeEnum: DataTypeEnum = DataTypeEnum.Companion.urlOf(jdbcUrl)
                ?: throw RuntimeException("Not supported data type: jdbcUrl=$jdbcUrl")
            druidDataSource.driverClassName = dataTypeEnum.driver
        } else {
            druidDataSource.driverClassName = className
        }
        druidDataSource.url = jdbcUrl
        druidDataSource.username = userName
        druidDataSource.password = password
        // 配置获取连接等待超时的时间
        druidDataSource.maxWait = 3000
        // 配置初始化大小、最小、最大
        druidDataSource.initialSize = 1
        druidDataSource.minIdle = 1
        druidDataSource.maxActive = 1

        // 如果链接出现异常则直接判定为失败而不是一直重试
        druidDataSource.isBreakAfterAcquireFailure = true
        try {
            druidDataSource.init()
        } catch (e: SQLException) {
            SqlUtils.log.error("Exception during pool initialization", e)
            throw RuntimeException(e.message)
        }
        return druidDataSource
    }

    private fun getConnection(jdbcUrl: String, userName: String, password: String): Connection? {
        val dataSource = getDataSource(jdbcUrl, userName, password)
        var connection: Connection? = null
        try {
            connection = dataSource.connection
        } catch (ignored: Exception) {
        }
        try {
            val timeOut = 5
            if (null == connection || connection.isClosed || !connection.isValid(timeOut)) {
                SqlUtils.log.info("connection is closed or invalid, retry get connection!")
                connection = dataSource.connection
            }
        } catch (e: Exception) {
            SqlUtils.log.error("create connection error, jdbcUrl: {}", jdbcUrl)
            throw RuntimeException("create connection error, jdbcUrl: $jdbcUrl")
        } finally {
            close(connection)
        }
        return connection
    }

    private fun releaseConnection(connection: Connection?) {
        if (null != connection) {
            try {
                connection.close()
            } catch (e: Exception) {
                SqlUtils.log.error(e.message, e)
                SqlUtils.log.error("connection close error：" + e.message)
            }
        }
    }

    fun testConnection(jdbcUrl: String, userName: String, password: String): Boolean {
        var connection: Connection? = null
        try {
            connection = getConnection(jdbcUrl, userName, password)
            if (null != connection) {
                return true
            }
        } catch (e: Exception) {
            SqlUtils.log.info("Get connection failed:" + e.message)
        } finally {
            releaseConnection(connection)
        }
        return false
    }

    fun executeFile(jdbcUrl: String, userName: String, password: String, sqlFile: File): String? {
        val connection = getConnection(jdbcUrl, userName, password)
        try {
            batchExecute(connection, readSqlList(sqlFile))
        } catch (e: Exception) {
            SqlUtils.log.error("sql脚本执行发生异常:{}", e.message)
            return e.message
        } finally {
            releaseConnection(connection)
        }
        return "success"
    }

    /**
     * 批量执行sql
     * @param connection /
     * @param sqlList /
     */
    fun batchExecute(connection: Connection?, sqlList: List<String>) {
        var st: Statement? = null
        try {
            st = connection!!.createStatement()
            for (sql in sqlList) {
                if (sql.endsWith(";")) {
                    sql = sql.substring(0, sql.length - 1)
                }
                st.addBatch(sql)
            }
            st.executeBatch()
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        } finally {
            close(st)
        }
    }

    /**
     * 将文件中的sql语句以；为单位读取到列表中
     * @param sqlFile /
     * @return /
     * @throws Exception e
     */
    @Throws(Exception::class)
    private fun readSqlList(sqlFile: File): List<String> {
        val sqlList: MutableList<String> = Lists.newArrayList()
        val sb = StringBuilder()
        BufferedReader(
            InputStreamReader(
                FileInputStream(sqlFile), StandardCharsets.UTF_8
            )
        ).use { reader ->
            var tmp: String
            while (reader.readLine().also { tmp = it } != null) {
                SqlUtils.log.info("line:{}", tmp)
                if (tmp.endsWith(";")) {
                    sb.append(tmp)
                    sqlList.add(sb.toString())
                    sb.delete(0, sb.length)
                } else {
                    sb.append(tmp)
                }
            }
            if (!"".endsWith(sb.toString().trim { it <= ' ' })) {
                sqlList.add(sb.toString())
            }
        }
        return sqlList
    }
}
/*
 * <<
 *  Davinci
 *  ==
 *  Copyright (C) 2016 - 2019 EDP
 *  ==
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  >>
 *
 */
package me.zhengjie.modules.mnt.util

import lombok.extern.slf4j.Slf4jimport

java.util.*
/**
 * @author /
 */
@Slf4j
enum class DataTypeEnum(
    val feature: String,
    val desc: String,
    val driver: String,
    val keywordPrefix: String,
    val keywordSuffix: String,
    val aliasPrefix: String,
    val aliasSuffix: String
) {
    /** mysql  */
    MYSQL("mysql", "mysql", "com.mysql.jdbc.Driver", "`", "`", "'", "'"),

    /** oracle  */
    ORACLE("oracle", "oracle", "oracle.jdbc.driver.OracleDriver", "\"", "\"", "\"", "\""),

    /** sql server  */
    SQLSERVER("sqlserver", "sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "\"", "\"", "\"", "\""),

    /** h2  */
    H2("h2", "h2", "org.h2.Driver", "`", "`", "\"", "\""),

    /** phoenix  */
    PHOENIX("phoenix", "hbase phoenix", "org.apache.phoenix.jdbc.PhoenixDriver", "", "", "\"", "\""),

    /** mongo  */
    MONGODB("mongo", "mongodb", "mongodb.jdbc.MongoDriver", "`", "`", "\"", "\""),

    /** sql4es  */
    ELASTICSEARCH("sql4es", "elasticsearch", "nl.anchormen.sql4es.jdbc.ESDriver", "", "", "'", "'"),

    /** presto  */
    PRESTO("presto", "presto", "com.facebook.presto.jdbc.PrestoDriver", "", "", "\"", "\""),

    /** moonbox  */
    MOONBOX("moonbox", "moonbox", "moonbox.jdbc.MbDriver", "`", "`", "`", "`"),

    /** cassandra  */
    CASSANDRA("cassandra", "cassandra", "com.github.adejanovski.cassandra.jdbc.CassandraDriver", "", "", "'", "'"),

    /** click house  */
    CLICKHOUSE("clickhouse", "clickhouse", "ru.yandex.clickhouse.ClickHouseDriver", "", "", "\"", "\""),

    /** kylin  */
    KYLIN("kylin", "kylin", "org.apache.kylin.jdbc.Driver", "\"", "\"", "\"", "\""),

    /** vertica  */
    VERTICA("vertica", "vertica", "com.vertica.jdbc.Driver", "", "", "'", "'"),

    /** sap  */
    HANA("sap", "sap hana", "com.sap.db.jdbc.Driver", "", "", "'", "'"),

    /** impala  */
    IMPALA("impala", "impala", "com.cloudera.impala.jdbc41.Driver", "", "", "'", "'");

    companion object {
        private const val JDBC_URL_PREFIX = "jdbc:"
        fun urlOf(jdbcUrl: String): DataTypeEnum? {
            val url = jdbcUrl.lowercase(Locale.getDefault()).trim { it <= ' ' }
            for (dataTypeEnum in values()) {
                if (url.startsWith(JDBC_URL_PREFIX + dataTypeEnum.feature)) {
                    try {
                        val aClass = Class.forName(dataTypeEnum.driver)
                            ?: throw RuntimeException("Unable to get driver instance for jdbcUrl: $jdbcUrl")
                    } catch (e: ClassNotFoundException) {
                        throw RuntimeException("Unable to get driver instance: $jdbcUrl")
                    }
                    return dataTypeEnum
                }
            }
            return null
        }
    }
}
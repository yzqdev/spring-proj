package org.sang.utils

import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import org.apache.ibatis.type.TypeHandler
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.text.SimpleDateFormat

/**
 * Created by sang on 2017/12/25.
 */
@MappedJdbcTypes(JdbcType.DATE)
@MappedTypes(String::class)
class DateTypeHandler : TypeHandler<String?> {
    private val sdf = SimpleDateFormat("yyyy-MM-dd")
    @Throws(SQLException::class)
    override fun setParameter(ps: PreparedStatement, i: Int, parameter: String?, jdbcType: JdbcType) {
    }

    @Throws(SQLException::class)
    override fun getResult(rs: ResultSet, columnName: String): String? {
        return sdf.format(rs.getDate(columnName))
    }

    @Throws(SQLException::class)
    override fun getResult(rs: ResultSet, columnIndex: Int): String? {
        return sdf.format(rs.getDate(columnIndex))
    }

    @Throws(SQLException::class)
    override fun getResult(cs: CallableStatement, columnIndex: Int): String? {
        return sdf.format(cs.getDate(columnIndex))
    }
}
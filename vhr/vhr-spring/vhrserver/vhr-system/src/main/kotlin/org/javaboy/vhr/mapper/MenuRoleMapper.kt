package org.javaboy.vhr.mapper

import org.apache.ibatis.annotations.Param

interface MenuRoleMapper {
    fun deleteByPrimaryKey(id: Int?): Int
    fun insert(record: MenuRole?): Int
    fun insertSelective(record: MenuRole?): Int
    fun selectByPrimaryKey(id: Int?): MenuRole?
    fun updateByPrimaryKeySelective(record: MenuRole?): Int
    fun updateByPrimaryKey(record: MenuRole?): Int
    fun deleteByRid(rid: Int?)
    fun insertRecord(@Param("rid") rid: Int?, @Param("mids") mids: Array<Int?>?): Int?
}
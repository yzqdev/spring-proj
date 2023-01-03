package org.javaboy.vhr.mapper

import org.apache.ibatis.annotations.Param

interface HrRoleMapper {
    fun deleteByPrimaryKey(id: Int?): Int
    fun insert(record: HrRole?): Int
    fun insertSelective(record: HrRole?): Int
    fun selectByPrimaryKey(id: Int?): HrRole?
    fun updateByPrimaryKeySelective(record: HrRole?): Int
    fun updateByPrimaryKey(record: HrRole?): Int
    fun deleteByHrid(hrid: Int?)
    fun addRole(@Param("hrid") hrid: Int?, @Param("rids") rids: Array<Int?>?): Int?
}
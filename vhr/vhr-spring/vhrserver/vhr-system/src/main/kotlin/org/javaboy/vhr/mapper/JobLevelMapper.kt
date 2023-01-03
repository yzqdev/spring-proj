package org.javaboy.vhr.mapper

import org.apache.ibatis.annotations.Param

interface JobLevelMapper {
    fun deleteByPrimaryKey(id: Int?): Int
    fun insert(record: JobLevel?): Int
    fun insertSelective(record: JobLevel?): Int
    fun selectByPrimaryKey(id: Int?): JobLevel?
    fun updateByPrimaryKeySelective(record: JobLevel?): Int
    fun updateByPrimaryKey(record: JobLevel?): Int
    val allJobLevels: List<Any?>?
    fun deleteJobLevelsByIds(@Param("ids") ids: Array<Int?>?): Int?
}
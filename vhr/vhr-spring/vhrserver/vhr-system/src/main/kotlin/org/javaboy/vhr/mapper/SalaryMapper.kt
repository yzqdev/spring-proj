package org.javaboy.vhr.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper

interface SalaryMapper : BaseMapper<Salary?> {
    fun deleteByPrimaryKey(id: Int?): Int
    fun insert(record: Salary?): Int
    fun insertSelective(record: Salary?): Int
    fun selectByPrimaryKey(id: Int?): Salary?
    fun updateByPrimaryKeySelective(record: Salary?): Int
    fun updateByPrimaryKey(record: Salary?): Int
    val allSalaries: List<Any?>?
}
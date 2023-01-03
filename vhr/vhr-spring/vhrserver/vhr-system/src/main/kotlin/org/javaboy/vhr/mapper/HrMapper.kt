package org.javaboy.vhr.mapper

org.javaboy.vhr.model.Role
interface HrMapper {
    fun deleteByPrimaryKey(id: Int?): Int
    fun insert(record: Hr?): Int
    fun insertSelective(record: Hr?): Int
    fun selectByPrimaryKey(id: Int?): Hr?
    fun updateByPrimaryKeySelective(record: Hr?): Int
    fun updateByPrimaryKey(record: Hr?): Int
    fun loadUserByUsername(username: String?): Hr?
    fun getHrRolesById(id: Int?): List<Role?>?
    fun getAllHrs(@Param("hrid") hrid: Int?, @Param("keywords") keywords: String?): List<Hr?>?
    fun getAllHrsExceptCurrentHr(id: Int?): List<Hr?>?
    fun updatePasswd(@Param("hrid") hrid: Int?, @Param("encodePass") encodePass: String?): Int?
    fun updateUserface(@Param("url") url: String?, @Param("id") id: Int?): Int?
}
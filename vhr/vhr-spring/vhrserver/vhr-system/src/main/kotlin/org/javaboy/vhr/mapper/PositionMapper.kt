package org.javaboy.vhr.mapper

org.javaboy.vhr.model.Position
interface PositionMapper {
    fun deleteByPrimaryKey(id: Int?): Int
    fun insert(record: Position?): Int
    fun insertSelective(record: Position?): Int
    fun selectByPrimaryKey(id: Int?): Position?
    fun updateByPrimaryKeySelective(record: Position?): Int
    fun updateByPrimaryKey(record: Position?): Int
    val allPositions: List<Position?>?
    fun deletePositionsByIds(@Param("ids") ids: Array<Int?>?): Int?
}
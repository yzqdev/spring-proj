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
package me.zhengjie.service.impl

import cn.hutool.core.collection.CollectionUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.ZipUtil
import lombok.RequiredArgsConstructor
import me.zhengjie.domain.ColumnInfo
import me.zhengjie.domain.GenConfig
import me.zhengjie.domain.vo.TableInfo
import me.zhengjie.exception.BadRequestException
import me.zhengjie.repository.ColumnInfoRepository
import me.zhengjie.service.GeneratorService
import me.zhengjie.utils.FileUtil.downloadFile
import me.zhengjie.utils.GenUtil
import me.zhengjie.utils.PageUtil.toPage
import me.zhengjie.utils.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.util.stream.Collectors
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2019-01-02
 */
@Service
@RequiredArgsConstructor
class GeneratorServiceImpl : GeneratorService {
    @PersistenceContext
    private val em: EntityManager? = null
    private val columnInfoRepository: ColumnInfoRepository? = null

    // 使用预编译防止sql注入
    override val tables: Any?
        get() {
            // 使用预编译防止sql注入
            val sql =
                "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables " +
                        "where table_schema = (select database()) " +
                        "order by create_time desc"
            val query = em!!.createNativeQuery(sql)
            return query.resultList
        }

    override fun getTables(name: String, startEnd: IntArray): Any {
        // 使用预编译防止sql注入
        val sql =
            "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables " +
                    "where table_schema = (select database()) " +
                    "and table_name like :table order by create_time desc"
        val query = em!!.createNativeQuery(sql)
        query.firstResult = startEnd[0]
        query.maxResults = startEnd[1] - startEnd[0]
        query.setParameter("table", if (StringUtils.isNotBlank(name)) "%$name%" else "%%")
        val result = query.resultList
        val tableInfos: MutableList<TableInfo> = ArrayList()
        for (obj in result) {
            val arr = obj as Array<Any>
            tableInfos.add(
                TableInfo(
                    arr[0], arr[1], arr[2], arr[3], if (ObjectUtil.isNotEmpty(
                            arr[4]
                        )
                    ) arr[4] else "-"
                )
            )
        }
        val countSql = "select count(1) from information_schema.tables " +
                "where table_schema = (select database()) and table_name like :table"
        val queryCount = em.createNativeQuery(countSql)
        queryCount.setParameter("table", if (StringUtils.isNotBlank(name)) "%$name%" else "%%")
        val totalElements = queryCount.singleResult
        return toPage(tableInfos, totalElements)
    }

    override fun getColumns(tableName: String?): List<ColumnInfo?>? {
        var columnInfos = columnInfoRepository!!.findByTableNameOrderByIdAsc(tableName)
        return if (CollectionUtil.isNotEmpty(columnInfos)) {
            columnInfos
        } else {
            columnInfos = query(tableName)
            columnInfoRepository.saveAll(columnInfos)
        }
    }

    override fun query(tableName: String?): List<ColumnInfo?> {
        // 使用预编译防止sql注入
        val sql =
            "select column_name, is_nullable, data_type, column_comment, column_key, extra from information_schema.columns " +
                    "where table_name = ? and table_schema = (select database()) order by ordinal_position"
        val query = em!!.createNativeQuery(sql)
        query.setParameter(1, tableName)
        val result = query.resultList
        val columnInfos: MutableList<ColumnInfo?> = ArrayList()
        for (obj in result) {
            val arr = obj as Array<Any>
            columnInfos.add(
                ColumnInfo(
                    tableName!!,
                    arr[0].toString(), "NO" == arr[1],
                    arr[2].toString(),
                    (if (ObjectUtil.isNotNull(arr[3])) arr[3].toString() else null)!!,
                    (if (ObjectUtil.isNotNull(arr[4])) arr[4].toString() else null)!!,
                    (if (ObjectUtil.isNotNull(arr[5])) arr[5].toString() else null)!!
                )
            )
        }
        return columnInfos
    }

    override fun sync(columnInfos: List<ColumnInfo>, columnInfoList: List<ColumnInfo>) {
        // 第一种情况，数据库类字段改变或者新增字段
        for (columnInfo in columnInfoList) {
            // 根据字段名称查找
            val columns: List<ColumnInfo?> =
                columnInfos.stream().filter { c: ColumnInfo -> c.getColumnName().equals(columnInfo.getColumnName()) }
                    .collect(Collectors.toList())
            // 如果能找到，就修改部分可能被字段
            if (CollectionUtil.isNotEmpty(columns)) {
                val column = columns[0]!!
                column.setColumnType(columnInfo.getColumnType())
                column.setExtra(columnInfo.getExtra())
                column.setKeyType(columnInfo.getKeyType())
                if (StringUtils.isBlank(column.getRemark())) {
                    column.setRemark(columnInfo.getRemark())
                }
                columnInfoRepository!!.save(column)
            } else {
                // 如果找不到，则保存新字段信息
                columnInfoRepository!!.save(columnInfo)
            }
        }
        // 第二种情况，数据库字段删除了
        for (columnInfo in columnInfos) {
            // 根据字段名称查找
            val columns: List<ColumnInfo?> =
                columnInfoList.stream().filter { c: ColumnInfo -> c.getColumnName().equals(columnInfo.getColumnName()) }
                    .collect(Collectors.toList())
            // 如果找不到，就代表字段被删除了，则需要删除该字段
            if (CollectionUtil.isEmpty(columns)) {
                columnInfoRepository!!.delete(columnInfo)
            }
        }
    }

    override fun save(columnInfos: List<ColumnInfo?>) {
        columnInfoRepository!!.saveAll<ColumnInfo>(columnInfos)
    }

    override fun generator(genConfig: GenConfig, columns: List<ColumnInfo?>?) {
        if (genConfig.getId() == null) {
            throw BadRequestException("请先配置生成器")
        }
        try {
            GenUtil.generatorCode(columns, genConfig)
        } catch (e: IOException) {
            log.error(e.message, e)
            throw BadRequestException("生成失败，请手动处理已生成的文件")
        }
    }

    override fun preview(genConfig: GenConfig, columns: List<ColumnInfo?>?): ResponseEntity<Any> {
        if (genConfig.getId() == null) {
            throw BadRequestException("请先配置生成器")
        }
        val genList = GenUtil.preview(columns, genConfig)
        return ResponseEntity(genList, HttpStatus.OK)
    }

    override fun download(
        genConfig: GenConfig,
        columns: List<ColumnInfo?>?,
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ) {
        if (genConfig.getId() == null) {
            throw BadRequestException("请先配置生成器")
        }
        try {
            val file = File(GenUtil.download(columns, genConfig))
            val zipPath = file.path + ".zip"
            ZipUtil.zip(file.path, zipPath)
            downloadFile(request!!, response!!, File(zipPath), true)
        } catch (e: IOException) {
            throw BadRequestException("打包失败")
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(GeneratorServiceImpl::class.java)
    }
}
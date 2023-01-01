package im.zhaojun.zfile.util

import cn.hutool.core.comparator.CompareUtil
import im.zhaojun.zfile.model.dto.FileItemDTO
import im.zhaojun.zfile.model.enums.FileTypeEnum

/**
 * 文件比较器
 *
 * - 文件夹始终比文件排序高
 * - 默认按照名称排序
 * - 默认排序为升序
 * - 按名称排序不区分大小写
 *
 * @author zhaojun
 */
class FileComparator : Comparator<FileItemDTO> {
    private var sortBy: String? = null
    private var order: String? = null

    constructor() {}
    constructor(sortBy: String?, order: String?) {
        this.sortBy = sortBy
        this.order = order
    }

    override fun compare(o1: FileItemDTO, o2: FileItemDTO): Int {
        if (sortBy == null) {
            sortBy = "name"
        }
        if (order == null) {
            order = "asc"
        }
        val o1Type = o1.type
        val o2Type = o2.type
        val naturalOrderComparator = NaturalOrderComparator()
        if (o1Type == o2Type) {
            val result: Int
            result = when (sortBy) {
                "time" -> CompareUtil.compare(o1.time, o2.time)
                "size" -> CompareUtil.compare(o1.size, o2.size)
                else -> naturalOrderComparator.compare(o1.name, o2.name)
            }
            return if ("asc" == order) result else -result
        }
        return if (o1Type == FileTypeEnum.FOLDER) {
            -1
        } else {
            1
        }
    }
}
package im.zhaojun.zfile.service.base

import im.zhaojun.zfile.cache.ZFileCache
import im.zhaojun.zfile.exception.InitializeDriveException
import im.zhaojun.zfile.model.dto.FileItemDTO
import im.zhaojun.zfile.model.entity.StorageConfig
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.service.baseimport.BaseFileService
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value


/**
 * @author zhaojun
 */
@Slf4j
abstract class AbstractBaseFileService( private val zFileCache: ZFileCache) : BaseFileService {


    /**
     * 下载链接过期时间, 目前只在兼容 S3 协议的存储策略中使用到.
     */
    @Value("\${zfile.cache.timeout}")
    protected var timeout: Long? = null
    /**
     * 获取是否初始化成功
     *
     * @return  初始化成功与否
     */
    /**
     * 是否初始化成功
     */
    var isInitialized = false
        protected set

    /**
     * 基路径
     */
    protected var basePath: String? = null

    /**
     * 驱动器 ID
     */
    @kotlin.jvm.JvmField
    var driveId: String? = null

    /***
     * 获取指定路径下的文件及文件夹, 默认缓存 60 分钟，每隔 30 分钟刷新一次.
     *
     * @param   path
     * 文件路径
     *
     * @return  文件及文件夹列表
     *
     * @throws Exception  获取文件列表中出现的异常
     */
    @Throws(Exception::class)
    abstract override fun fileList(path: String): List<FileItemDTO>

    /**
     * 清理当前存储策略的缓存
     */
    fun clearFileCache() {
        zFileCache!!.clear(driveId)
    }

    /**
     * 初始化方法, 启动时自动调用实现类的此方法进行初始化.
     *
     * @param   driveId
     * 驱动器 ID
     */
    abstract fun init(driveId: String?)

    /**
     * 测试是否连接成功, 会尝试取调用获取根路径的文件, 如果没有抛出异常, 则认为连接成功, 某些存储策略需要复写此方法.
     */
    protected fun testConnection() {
        try {
            fileList("/")
        } catch (e: Exception) {
            throw InitializeDriveException("初始化异常, 错误信息为: " + e.message, e)
        }
    }

    /**
     * 获取是否初始化成功
     *
     * @return  初始化成功与否
     */
    val isUnInitialized: Boolean
        get() = !isInitialized

    /**
     * 获取当前实现类的存储策略类型
     *
     * @return  存储策略类型枚举对象
     */
    abstract val storageTypeEnum: StorageTypeEnum

    /**
     * 获取初始化当前存储策略, 所需要的参数信息 (用于表单填写)
     *
     * @return  初始化所需的参数列表
     */
    abstract fun storageStrategyConfigList(): List<StorageConfig>

    /**
     * 合并数据库查询到的驱动器参数和驱动器本身支持的参数列表, 防止获取新增参数字段时出现空指针异常
     *
     * @param   dbStorageConfigList
     * 数据库查询到的存储列表
     */
    fun mergeStrategyConfig(dbStorageConfigList: MutableMap<String, StorageConfig?>) {
        // 获取驱动器支持的参数列表
        val storageConfigs = storageStrategyConfigList()

        // 比对数据库已存储的参数列表和驱动器支持的参数列表, 找出新增的支持项
        val dbConfigKeySet: Set<String> = dbStorageConfigList.keys
        val allKeySet = storageConfigs.stream().map { obj: StorageConfig -> obj.key }.collect(Collectors.toSet())
        allKeySet.removeAll(dbConfigKeySet)

        // 对于新增的参数, put 到数据库查询的 Map 中, 防止程序获取时出现 NPE.
        for (key in allKeySet) {
            val storageConfig = StorageConfig()
            storageConfig.value = ""
            dbStorageConfigList[key] = storageConfig
        }
    }

    /**
     * 搜索文件
     *
     * @param   name
     * 文件名
     *
     * @return  包含该文件名的所有文件或文件夹
     */
    fun search(name: String?): List<FileItemDTO> {
        return zFileCache!!.find(driveId, name)
    }

    /**
     * 获取单个文件信息
     *
     * @param   path
     * 文件路径
     *
     * @return  单个文件的内容.
     */
    abstract fun getFileItem(path: String): FileItemDTO?
}
package im.zhaojun.zfile.service.base

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ListObjectsRequest
import im.zhaojun.zfile.model.dto.FileItemDTO
import im.zhaojun.zfile.model.enums.FileTypeEnum
import im.zhaojun.zfile.serviceimport.StorageConfigService
import im.zhaojun.zfile.util.StringUtils.concatUrl
import im.zhaojun.zfile.util.StringUtils.generatorLink
import im.zhaojun.zfile.util.StringUtils.getFullPath
import im.zhaojun.zfile.util.StringUtils.removeFirstSeparator
import javax.annotation.Resource


/**
 * @author zhaojun
 */
abstract class AbstractS3BaseFileService(  ) : AbstractBaseFileService() {
@Resource
protected lateinit var storageConfigService: StorageConfigService
    protected var path: String? = null
    protected var bucketName: String? = null
    protected var domain: String? = null
    protected var s3Client: AmazonS3? = null
    protected var isPrivate = false
    override fun fileList(path: String): List<FileItemDTO> {
        this.path = path
        return s3FileList(path)
    }

    override fun getDownloadUrl(path: String?): String? {
        this.path = path
        return s3ObjectUrl(path)
    }

    /**
     * 获取 S3 指定目录下的对象列表
     * @param path      路径
     * @return  指定目录下的对象列表
     */
    fun s3FileList(path: String?): List<FileItemDTO> {
        var path = path
        path = removeFirstSeparator(path!!)
        val fullPath = removeFirstSeparator(
            getFullPath(
                basePath!!, path
            )!!
        )
        val fileItemList: MutableList<FileItemDTO> = ArrayList()
        val objectListing = s3Client!!.listObjects(ListObjectsRequest(bucketName, fullPath, "", "/", 1000))
        for (s in objectListing.objectSummaries) {
            val fileItemDTO = FileItemDTO()
            if (s.key == fullPath) {
                continue
            }
            fileItemDTO.name = s.key.substring(fullPath.length)
            fileItemDTO.size = s.size
            fileItemDTO.time = s.lastModified
            fileItemDTO.type = FileTypeEnum.FILE
            fileItemDTO.path = path
            val fullPathAndName = concatUrl(path, fileItemDTO.name)
            val directlink = generatorLink(driveId!!, fullPathAndName)
            fileItemDTO.src = directlink
            fileItemList.add(fileItemDTO)
        }
        for (commonPrefix in objectListing.commonPrefixes) {
            val fileItemDTO = FileItemDTO()
            if (commonPrefix == "/") {
                continue
            }
            fileItemDTO.name = commonPrefix.substring(fullPath.length, commonPrefix.length - 1)
            fileItemDTO.type = FileTypeEnum.FOLDER
            fileItemDTO.path = path
            fileItemList.add(fileItemDTO)
        }
        return fileItemList
    }

    /**
     * 获取对象的访问链接, 如果指定了域名, 则替换为自定义域名.
     * @return  S3 对象访问地址
     */
    fun s3ObjectUrl(path: String?): String {
        basePath = if (basePath == null) "" else basePath
        val fullPath = removeFirstSeparator(removeDuplicateSeparator(basePath + ZFileConstant.PATH_SEPARATOR + path)!!)

        // 如果不是私有空间, 且指定了加速域名, 则直接返回下载地址.
        if (BooleanUtil.isFalse(isPrivate) && isNotNullOrEmpty(domain)) {
            return concatPath(domain, fullPath)
        }
        val expirationDate = Date(System.currentTimeMillis() + timeout!! * 1000)
        val url = s3Client!!.generatePresignedUrl(bucketName, fullPath, expirationDate)
        var defaultUrl = url.toExternalForm()
        if (isNotNullOrEmpty(domain)) {
            defaultUrl = URLUtil.completeUrl(domain, url.file)
        }
        return URLUtil.decode(defaultUrl)
    }

    override fun getFileItem(path: String): FileItemDTO? {
        val list: List<FileItemDTO>
        list = try {
            val end = path.lastIndexOf("/")
            fileList(path.substring(0, end + 1))
        } catch (e: Exception) {
            throw NotExistFileException()
        }
        for (fileItemDTO in list) {
            val fullPath = concatUrl(fileItemDTO.path, fileItemDTO.name)
            if (fullPath == path) {
                fileItemDTO.src = getDownloadUrl(path)
                return fileItemDTO
            }
        }
        throw NotExistFileException()
    }
}
package im.zhaojun.zfile.model.enums

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

/**
 * @author zhaojun
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class StorageTypeEnum(var key: String, var description: String) {
    /**
     * 当前系统支持的所有存储策略
     */
    LOCAL("local", "本地存储"), ALIYUN("aliyun", "阿里云 OSS"), TENCENT("tencent", "腾讯云 COS"), UPYUN(
        "upyun",
        "又拍云 USS"
    ),
    FTP("ftp", "FTP"), UFILE("ufile", "UFile"), HUAWEI("huawei", "华为云 OBS"), MINIO("minio", "MINIO"), S3(
        "s3",
        "S3通用协议"
    ),
    ONE_DRIVE("onedrive", "OneDrive"), ONE_DRIVE_CHINA(
        "onedrive-china",
        "OneDrive 世纪互联"
    ),
    SHAREPOINT_DRIVE("sharepoint", "SharePoint"), SHAREPOINT_DRIVE_CHINA(
        "sharepoint-china",
        "SharePoint 世纪互联"
    ),
    QINIU("qiniu", "七牛云 KODO");

    companion object {
        private val enumMap: MutableMap<String, StorageTypeEnum> = HashMap()

        init {
            for (type in values()) {
                enumMap[im.zhaojun.zfile.model.enums.type.getKey()] = im.zhaojun.zfile.model.enums.type
            }
        }

        fun getEnum(value: String?): StorageTypeEnum? {
            return enumMap[value!!.lowercase(Locale.getDefault())]
        }
    }
}
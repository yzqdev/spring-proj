package cn.hellohao.quartz.job

import cn.hellohao.model.entity.StorageKey
import cn.hellohao.service.ImgService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.annotation.Resource

@Component
@EnableScheduling
class FirstJob {
    @Resource
    private val imgService: ImgService? = null

    @Resource
    private val nosImageupload: NOSImageupload? = null

    @Resource
    private val ossImageupload: OSSImageupload? = null

    @Resource
    private val cosImageupload: COSImageupload? = null

    @Resource
    private val kodoImageupload: KODOImageupload? = null

    @Resource
    private val ussImageupload: USSImageupload? = null

    @Resource
    private val uFileImageupload: UFileImageupload? = null

    @Resource
    private val ftpImageupload: FTPImageupload? = null

    @Resource
    var imgTempService: ImgTempService? = null

    @Resource
    var keysService: KeysService? = null
    @PostConstruct
    fun init() {
        firstJob = this
    }

    //调用的任务主体
    fun task() {
//		System.out.println("定时任务.//开始");
        try {
            val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val currdate = format.format(LocalDateTime.now())
            val imagesList: List<Images> = imgTempService.selectDelImgUidList(currdate)
            if (imagesList.size == 0) {
//				System.out.println("定时任务.//没有期限图片");
                return
            }
            for (images in imagesList) {
                imgTempService.delImgAndExp(images.getImgUid())
                imgService.deleimgForImgUid(images.getImgUid())
                val storageKey: StorageKey = keysService.selectKeys(images.getSource())
                if (storageKey.storageType == 1) {
                    firstJob!!.nosImageupload.delNOS(storageKey.id, images.getImgName())
                } else if (storageKey.storageType == 2) {
                    firstJob!!.ossImageupload.delOSS(storageKey.id, images.getImgName())
                } else if (storageKey.storageType == 3) {
                    firstJob!!.ussImageupload.delUSS(storageKey.id, images.getImgName())
                } else if (storageKey.storageType == 4) {
                    firstJob!!.kodoImageupload.delKODO(storageKey.id, images.getImgName())
                } else if (storageKey.storageType == 5) {
                    LocUpdateImg.deleteLOCImg(images.getImgName())
                } else if (storageKey.storageType == 6) {
                    firstJob!!.cosImageupload.delCOS(storageKey.id, images.getImgName())
                } else if (storageKey.storageType == 7) {
                    firstJob!!.ftpImageupload.delFTP(storageKey.id, images.getImgName())
                } else if (storageKey.storageType == 8) {
                    firstJob!!.uFileImageupload.delUFile(storageKey.id, images.getImgName())
                } else {
                    System.err.println("未获取到对象存储参数，上传失败。")
                }
            }
        } catch (e: Exception) {
            System.err.println("任务查询期限图像发生错误")
            e.printStackTrace()
        }
    }

    companion object {
        private var firstJob: FirstJob? = null
    }
}
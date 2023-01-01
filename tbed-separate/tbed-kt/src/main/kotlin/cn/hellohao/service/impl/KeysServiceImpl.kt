package cn.hellohao.service.impl

import cn.hellohao.mapper.KeysMapper
import cn.hellohao.model.entity.StorageKey
import cn.hellohao.service.implimport.NOSImageupload
import cn.hellohao.service.implimport.OSSImageupload
import cn.hellohao.service.implimport.UFileImageupload
import cn.hellohao.service.implimport.USSImageupload
import cn.hellohao.serviceimport.KeysService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service


@Service
@RequiredArgsConstructor
class KeysServiceImpl : ServiceImpl<KeysMapper?, StorageKey?>(), KeysService {
    private val keysMapper: KeysMapper? = null
    private val nOSImageupload: NOSImageupload? = null
    private val ossImageupload: OSSImageupload? = null
    private val ussImageupload: USSImageupload? = null
    private val kodoImageupload: KODOImageupload? = null
    private val cosImageupload: COSImageupload? = null
    private val ftpImageupload: FTPImageupload? = null
    private val uFileImageupload: UFileImageupload? = null
    override fun selectKeys(id: String?): StorageKey {
        // TODO Auto-generated method stub
        return keysMapper.selectKeys(id)
    }

    val storageName: List<StorageKey?>
        get() = keysMapper.getStorageName()
    val storage: List<StorageKey?>
        get() = keysMapper.getStorage()

    override fun updateKey(key: StorageKey): Msg {
        val msg = Msg()
        var ret = -2
        //修改完初始化
        if (key.storageType == 1) {
            ret = NOSImageupload.Companion.Initialize(key) //实例化网易
        } else if (key.storageType == 2) {
            ret = OSSImageupload.Companion.Initialize(key)
        } else if (key.storageType == 3) {
            ret = USSImageupload.Companion.Initialize(key)
        } else if (key.storageType == 4) {
            ret = KODOImageupload.Companion.Initialize(key)
        } else if (key.storageType == 5) {
            ret = 1
        } else if (key.storageType == 6) {
            ret = COSImageupload.Companion.Initialize(key)
        } else if (key.storageType == 7) {
            ret = FTPImageupload.Companion.Initialize(key)
        } else if (key.storageType == 8) {
            ret = UFileImageupload.Companion.Initialize(key)
        } else {
            Print.Normal("为获取到存储参数，或者使用存储源是本地的。")
        }
        if (ret > 0) {
            keysMapper.updateById(key)
            msg.setInfo("保存成功")
        } else {
            if (key.storageType == 5) {
                keysMapper.updateById(key)
                msg.setInfo("保存成功")
            } else {
                msg.setCode("4002")
                msg.setInfo("对象存储初始化失败,请检查参数是否正确")
            }
        }
        return msg
    }

    val keys: List<StorageKey?>
        get() = keysMapper.getKeys()
}
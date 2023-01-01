package cn.hellohao.service.impl

import cn.hellohao.mapper.SysConfigMapper
import cn.hellohao.model.entity.SysConfig
import cn.hellohao.service.SysConfigService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/15 13:48
 */
@Service
class SysConfigServiceImpl(private val sysConfigMapper: SysConfigMapper) : ServiceImpl<SysConfigMapper?, SysConfig?>(), SysConfigService {

    override fun getstate(): SysConfig? {
        return sysConfigMapper!!.defaultSysConfig
    }

    override fun setstate(sysConfig: SysConfig?): Int? {
        return sysConfigMapper!!.setDefaultSysConfig(sysConfig)
    }
}
package cn.hellohao.service

import cn.hellohao.model.entity.SysConfig
import com.baomidou.mybatisplus.extension.service.IService
import org.springframework.stereotype.Service


/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/15 13:46
 */
@Service
interface SysConfigService : IService<SysConfig?> {
    fun getstate(): SysConfig?
    fun setstate(sysConfig: SysConfig?): Int?
}
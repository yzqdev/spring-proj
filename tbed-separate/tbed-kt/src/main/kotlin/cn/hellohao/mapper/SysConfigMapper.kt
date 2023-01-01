package cn.hellohao.mapper

import cn.hellohao.model.entity.SysConfig
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/15 13:33
 */
@Mapper
interface SysConfigMapper : BaseMapper<SysConfig?> {
    /**
     * getstate
     *
     * @return [SysConfig]
     */
    val defaultSysConfig: SysConfig?

    /**
     * 设置状态
     *
     * @param sysConfig 系统配置
     * @return [Integer]
     */
    fun setDefaultSysConfig(sysConfig: SysConfig?): Int?
}
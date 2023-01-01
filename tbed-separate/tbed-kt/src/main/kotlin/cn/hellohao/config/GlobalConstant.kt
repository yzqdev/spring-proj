package cn.hellohao.config

import java.io.File

/**
 *
 * @author Hellohao
 *
 * @version 1.0
 *
 * @date 29/11/2021 上午 10:35
 */
object GlobalConstant {
    var SYSTYPE = "LINUX"
    var props = System.getProperties()
    var LOCPATH = props.getProperty("user.home") + File.separator + "HellohaoData"
}
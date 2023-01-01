package cn.hellohao.config

/**
 * @author Hellohao
 * @version 1.0
 * @date 2020/5/15 9:20
 */
object SysName {
    const val SYSNAME = "root,hellohaocheck,selectdomain,image,hellohaocheck,HellohaoData,TOIMG," +
            "user,users,admin,retrievepass,deleteimg,hellohaotempimg,360,hellohaotempwatermarimg,components,log"

    fun CheckSysName(name: String?): Boolean {
        var b = true
        if (SYSNAME.contains(name!!)) {
            b = false
        }
        return b
    }
}
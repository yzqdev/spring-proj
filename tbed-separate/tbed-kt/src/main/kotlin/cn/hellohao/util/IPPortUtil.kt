package cn.hellohao.util

import java.lang.management.ManagementFactory
import java.net.InetAddress
import javax.management.MalformedObjectNameException
import javax.management.ObjectName
import javax.management.Query

object IPPortUtil {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        Print.warning(localIP)
        Print.warning(localPort)
    }//throws MalformedObjectNameException

    /**
     * @return
     * @throws MalformedObjectNameException
     * 获取当前机器的端口号
     */
    val localPort: String
        get() { //throws MalformedObjectNameException
            val beanServer =
                ManagementFactory.getPlatformMBeanServer()
            var objectNames: Set<ObjectName>? = null
            try {
                objectNames = beanServer.queryNames(
                    ObjectName("*:type=Connector,*"),
                    Query.match(
                        Query.attr("protocol"),
                        Query.value("HTTP/1.1")
                    )
                )
            } catch (e: MalformedObjectNameException) {
                e.printStackTrace()
            }
            return objectNames!!.iterator().next().getKeyProperty("port")
        }

    /**
     * @return
     * 获取当前机器的IP
     */
    val localIP: String
        get() {
            var addr: InetAddress? = null
            try {
                addr = InetAddress.getLocalHost()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val ipAddr = addr!!.address
            var ipAddrStr = ""
            for (i in ipAddr.indices) {
                if (i > 0) {
                    ipAddrStr += "."
                }
                ipAddrStr += ipAddr[i].toInt() and 0xFF
            }
            return ipAddrStr
        }
}
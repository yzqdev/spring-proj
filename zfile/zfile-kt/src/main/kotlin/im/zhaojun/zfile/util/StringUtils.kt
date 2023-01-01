package im.zhaojun.zfile.util

import cn.hutool.core.util.ObjectUtil
import im.zhaojun.zfile.model.constant.ZFileConstant
import im.zhaojun.zfile.serviceimport.SystemConfigService
import java.net.MalformedURLException
import java.net.URL

/**
 * @author zhaojun
 */
object StringUtils {
    const val DELIMITER = '/'
    const val DELIMITER_STR = "/"
    const val HTTP_PROTOCAL = "http://"
    const val HTTPS_PROTOCAL = "https://"

    /**
     * 移除 URL 中的第一个 '/'
     * @return 如 path = '/folder1/file1', 返回 'folder1/file1'
     */
    @JvmStatic
    fun removeFirstSeparator(path: String): String {
        var path = path
        if ("" != path && path[0] == DELIMITER) {
            path = path.substring(1)
        }
        return path
    }

    /**
     * 移除 URL 中的最后一个 '/'
     * @return 如 path = '/folder1/file1/', 返回 '/folder1/file1'
     */
    @JvmStatic
    fun removeLastSeparator(path: String): String {
        var path = path
        if ("" != path && path[path.length - 1] == DELIMITER) {
            path = path.substring(0, path.length - 1)
        }
        return path
    }

    fun concatUrl(path: String, name: String): String? {
        return removeDuplicateSeparator(DELIMITER.toString() + path + DELIMITER + name)
    }

    /**
     * 将域名和路径组装成 URL, 主要用来处理分隔符 '/'
     * @param domain    域名
     * @param path      路径
     * @return          URL
     */
    @JvmStatic
    fun concatPath(domain: String?, path: String?): String {
        var domain = domain
        var path = path
        if (path != null && path.length > 1 && path[0] != DELIMITER) {
            path = DELIMITER.toString() + path
        }
        if (domain != null && domain[domain.length - 1] == DELIMITER) {
            domain = domain.substring(0, domain.length - 2)
        }
        return domain + path
    }

    @JvmStatic
    fun removeDuplicateSeparator(path: String?): String? {
        if (path == null || path.length < 2) {
            return path
        }
        val sb = StringBuilder()
        if (path.indexOf(HTTP_PROTOCAL) == 0) {
            sb.append(HTTP_PROTOCAL)
        } else if (path.indexOf(HTTPS_PROTOCAL) == 0) {
            sb.append(HTTPS_PROTOCAL)
        }
        for (i in sb.length until path.length - 1) {
            val current = path[i]
            val next = path[i + 1]
            if (!(current == DELIMITER && next == DELIMITER)) {
                sb.append(current)
            }
        }
        sb.append(path[path.length - 1])
        return sb.toString()
    }

    @JvmStatic
    fun isNullOrEmpty(s: String?): Boolean {
        return s == null || "" == s
    }

    @JvmStatic
    fun isNotNullOrEmpty(s: String?): Boolean {
        return !isNullOrEmpty(s)
    }

    /**
     * 获取 basePath + path 的全路径地址.
     * @return basePath + path 的全路径地址.
     */
    @JvmStatic
    fun getFullPath(basePath: String, path: String): String? {
        var basePath = basePath
        var path = path
        basePath = ObjectUtil.defaultIfNull(basePath, "")
        path = ObjectUtil.defaultIfNull(path, "")
        return removeDuplicateSeparator(basePath + ZFileConstant.PATH_SEPARATOR + path)
    }

    /**
     * 替换 URL 中的 Host 部分，如替换 http://a.com/1.txt 为 https://abc.com/1.txt
     * @param   originUrl
     * 原 URL
     * @param   replaceHost
     * 替换的 HOST
     * @return  替换后的 URL
     */
    @JvmStatic
    fun replaceHost(originUrl: String?, replaceHost: String?): String? {
        try {
            val path = URL(originUrl).file
            return concatPath(replaceHost, path)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 拼接 URL，并去除重复的分隔符 '/'，但不会影响 http:// 和 https:// 这种头部
     * @param strs      拼接的字符数组
     * @return          拼接结果
     */
    @JvmStatic
    fun concatUrl(vararg strs: String?): String? {
        val sb = StringBuilder()
        for (i in strs.indices) {
            sb.append(strs[i])
            if (i != strs.size - 1) {
                sb.append(DELIMITER)
            }
        }
        return removeDuplicateSeparator(sb.toString())
    }

    /**
     * 拼接文件直链生成 URL
     * @param driveId       驱动器 ID
     * @param fullPath      文件全路径
     * @return              生成结果
     */
    @JvmStatic
    fun generatorLink(driveId: String, fullPath: String?): String? {
        val systemConfigService: SystemConfigService = SpringContextHolder.Companion.getBean<SystemConfigService>(
            SystemConfigService::class.java
        )
        val domain = systemConfigService.domain
        return concatUrl(domain, ZFileConstant.DIRECT_LINK_PREFIX, driveId, fullPath)
    }
}
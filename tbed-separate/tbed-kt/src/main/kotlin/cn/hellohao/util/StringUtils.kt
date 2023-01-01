package cn.hellohao.util

import cn.hellohao.model.entity.StorageKey

object StringUtils {
    //此方法目前未使用2020-04-06
    fun doNull(sourcekey: Int, k: StorageKey): Boolean {
        if (sourcekey == 1) {
            if (k.endpoint != null && k.accessSecret != null && k.endpoint != null && k.bucketName != null && k.requestAddress != null) {
                if (k.endpoint != "" && k.accessSecret != "" && k.endpoint != ""
                    && k.bucketName != "" && k.requestAddress != ""
                ) {
                    return true
                }
            }
        } else if (sourcekey == 2) {
            if (k.endpoint != null && k.accessSecret != null && k.endpoint != null && k.bucketName != null && k.requestAddress != null) {
                if (k.endpoint != "" && k.accessSecret != "" && k.endpoint != ""
                    && k.bucketName != "" && k.requestAddress != ""
                ) {
                    return true
                }
            }
        } else if (sourcekey == 3) {
            if (k.endpoint != null && k.accessSecret != null && k.bucketName != null && k.requestAddress != null) {
                if (k.endpoint != "" && k.accessSecret != ""
                    && k.bucketName != "" && k.requestAddress != ""
                ) {
                    return true
                }
            }
        } else if (sourcekey == 4) {
            if (k.endpoint != null && k.accessSecret != null && k.endpoint != null && k.bucketName != null && k.requestAddress != null) {
                if (k.endpoint != "" && k.accessSecret != "" && k.endpoint != ""
                    && k.bucketName != "" && k.requestAddress != ""
                ) {
                    return true
                }
            }
        } else if (sourcekey == 6) {
            if (k.endpoint != null && k.accessSecret != null && k.endpoint != null && k.bucketName != null && k.requestAddress != null) {
                if (k.endpoint != "" && k.accessSecret != "" && k.endpoint != ""
                    && k.bucketName != "" && k.requestAddress != ""
                ) {
                    return true
                }
            }
        } else if (sourcekey == 7) {
            if (k.endpoint != null && k.accessSecret != null && k.endpoint != null && k.requestAddress != null) {
                if (k.endpoint != "" && k.accessSecret != "" && k.endpoint != "" && k.requestAddress != "") {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 移除 URL 中的第一个 '/'
     * @return 如 path = '/folder1/file1', 返回 'folder1/file1'
     */
    fun removeFirstSeparator(path: String): String {
        var path = path
        if ("" != path && path[0] == '/') {
            path = path.substring(1)
        }
        return path
    }

    /**
     * 移除 URL 中的最后一个 '/'
     * @return 如 path = '/folder1/file1/', 返回 '/folder1/file1'
     */
    fun removeLastSeparator(path: String): String {
        var path = path
        if ("" != path && path[path.length - 1] == '/') {
            path = path.substring(0, path.length - 1)
        }
        return path
    }

    /**
     * 将域名和路径组装成 URL, 主要用来处理分隔符 '/'
     * @param domain    域名
     * @param path      路径
     * @return          URL
     */
    fun concatDomainAndPath(domain: String, path: String?): String {
        var domain = domain
        var path = path
        if (path != null && path.length > 1 && path[0] != '/') {
            path = "/$path"
        }
        if (domain[domain.length - 1] == '/') {
            domain = domain.substring(0, domain.length - 2)
        }
        return domain + path
    }
}
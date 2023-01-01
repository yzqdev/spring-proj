package cn.hellohao.util

import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import org.apache.commons.net.ftp.FTPReply
import org.apache.log4j.LogManager
import java.io.IOException

/**
 *
 * Title:
 *
 * Description:
 *
 * @author Hellohao
 * @date 2019年11月10日
 */
class FTPUtils(
    private val server: String,
    private val port: Int,
    private val userName: String,
    private val userPassword: String
) {
    private val LOGGER = LogManager.getLogger(javaClass)
    private var ftpClient: FTPClient? = null
    fun open(): Boolean {
        // 判断是否已连接
        return if (ftpClient != null && ftpClient!!.isConnected) {
            true
        } else try {
            ftpClient = FTPClient()
            // 连接FTP服务器
            ftpClient!!.connect(server, port)
            ftpClient!!.login(userName, userPassword)
            val reply = ftpClient!!.replyCode
            if (!FTPReply.isPositiveCompletion(reply)) {
                Print.warning("FTP服务器拒绝连接.")
                close()
                System.exit(1)
            }
            ftpClient!!.setFileType(FTP.BINARY_FILE_TYPE)
            ftpClient!!.enterLocalPassiveMode()
            true
        } catch (e: Exception) {
            close()
            e.printStackTrace()
            false
        }
    }

    /**
     *
     * Title: 切换到父目录
     *
     * Description:
     *
     * @author Hellohao
     * @date 2018年10月11日
     *
     * @return 切换结果 true：成功， false：失败
     */
    private fun changeToParentDir(): Boolean {
        return try {
            ftpClient!!.changeToParentDirectory()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     *
     * @param dir 目的目录
     * @return 切换结果 true：成功，false：失败
     */
    private fun cd(dir: String): Boolean {
        return try {
            ftpClient!!.changeWorkingDirectory(dir)
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     *
     * @param filePath 指定的目录
     * @return 文件列表,或者null
     */
    private fun getFileList(filePath: String): Array<FTPFile>? {
        return try {
            ftpClient!!.listFiles(filePath)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     *
     * @param ftpPath 目的目录
     * @return 切换结果
     */
    fun changeDir(ftpPath: String): Boolean {
        var ftpPath = ftpPath
        return if (!ftpClient!!.isConnected) {
            false
        } else try {
            val chars = ftpPath.toCharArray()
            val sbStr = StringBuffer(256)
            for (i in chars.indices) {
                if ('\\' == chars[i]) {
                    sbStr.append('/')
                } else {
                    sbStr.append(chars[i])
                }
            }
            ftpPath = sbStr.toString()
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftpClient!!.changeWorkingDirectory(String(ftpPath.toByteArray(), "UTF-8"))
            } else {
                // 多层目录循环创建
                val paths = ftpPath.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in paths.indices) {
                    ftpClient!!.changeWorkingDirectory(String(paths[i].toByteArray(), "UTF-8"))
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * @param ftpPath 需要创建的目录
     * @return
     */
    fun mkDir(ftpPath: String): Boolean {
        var ftpPath = ftpPath
        return if (!ftpClient!!.isConnected) {
            false
        } else try {
            // 将路径中的斜杠统一
            val chars = ftpPath.toCharArray()
            val sbStr = StringBuffer(256)
            for (i in chars.indices) {
                if ('\\' == chars[i]) {
                    sbStr.append('/')
                } else {
                    sbStr.append(chars[i])
                }
            }
            ftpPath = sbStr.toString()
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftpClient!!.makeDirectory(String(ftpPath.toByteArray(), "UTF-8"))
                ftpClient!!.changeWorkingDirectory(String(ftpPath.toByteArray(), "UTF-8"))
            } else {
                // 多层目录循环创建
                val paths = ftpPath.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in paths.indices) {
                    ftpClient!!.makeDirectory(String(paths[i].toByteArray(), "UTF-8"))
                    ftpClient!!.changeWorkingDirectory(String(paths[i].toByteArray(), "UTF-8"))
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     *
     * @param //localDirectoryAndFileName 本地文件目录和文件名
     * @param ftpFileName 上传到服务器的文件名()
     * @param ftpDirectory FTP目录如:/path1/pathb2/,如果目录不存在会自动创建目录(目录可以省略)
     * @return
     */
    fun upload(srcFile: File, ftpFileName: String?, ftpDirectory: String): Boolean {
        var ftpFileName = ftpFileName
        if (!ftpClient!!.isConnected) {
            return false
        }
        var flag = false
        if (ftpClient != null) {
            var fis: FileInputStream? = null
            try {
                fis = FileInputStream(srcFile)
                // 创建目录
                mkDir(ftpDirectory)
                ftpClient!!.bufferSize = 100000
                ftpClient!!.controlEncoding = "UTF-8"
                // 设置文件类型（二进制）
                ftpClient!!.setFileType(FTPClient.BINARY_FILE_TYPE)
                if (ftpFileName == null || ftpFileName === "") {
                    ftpFileName = srcFile.name
                }
                // 上传
                flag = ftpClient!!.storeFile(String(ftpFileName!!.toByteArray(), "UTF-8"), fis)
            } catch (e: Exception) {
                close()
                e.printStackTrace()
                return false
            } finally {
                try {
                    fis!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return flag
    }

    /**
     * @param ftpDirectoryAndFileName ftp服务器文件路径，以/dir形式开始
     * @param localDirectoryAndFileName 保存到本地的目录
     * @return
     */
    operator fun get(ftpDirectoryAndFileName: String, localDirectoryAndFileName: String?): Boolean {
        var ftpDirectoryAndFileName = ftpDirectoryAndFileName
        if (!ftpClient!!.isConnected) {
            return false
        }
        ftpClient!!.enterLocalPassiveMode()
        return try {
            // 将路径中的斜杠统一
            val chars = ftpDirectoryAndFileName.toCharArray()
            val sbStr = StringBuffer(256)
            for (i in chars.indices) {
                if ('\\' == chars[i]) {
                    sbStr.append('/')
                } else {
                    sbStr.append(chars[i])
                }
            }
            ftpDirectoryAndFileName = sbStr.toString()
            val filePath = ftpDirectoryAndFileName.substring(0, ftpDirectoryAndFileName.lastIndexOf("/"))
            val fileName = ftpDirectoryAndFileName.substring(ftpDirectoryAndFileName.lastIndexOf("/") + 1)
            changeDir(filePath)
            ftpClient!!.retrieveFile(
                String(fileName.toByteArray(), "UTF-8"),
                FileOutputStream(localDirectoryAndFileName)
            ) // download
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     *
     * @param pathName
     * @return
     */
    fun getFileNameList(pathName: String?): Array<String>? {
        return try {
            ftpClient!!.listNames(pathName)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     *
     * Title: 删除FTP上的文件
     *
     * Description:
     * @param ftpDirAndFileName 路径开头不能加/，比如应该是test/filename1
     * @return
     */
    fun deleteFile(ftpDirAndFileName: String?): Boolean {
        return if (!ftpClient!!.isConnected) {
            false
        } else try {
            ftpClient!!.deleteFile(ftpDirAndFileName)
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * @param ftpDirectory
     * @return
     */
    fun deleteDirectory(ftpDirectory: String?): Boolean {
        return if (!ftpClient!!.isConnected) {
            false
        } else try {
            ftpClient!!.removeDirectory(ftpDirectory)
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     *
     * Title: 关闭链接
     *
     * Description:
     */
    fun close() {
        try {
            if (ftpClient != null && ftpClient!!.isConnected) {
                ftpClient!!.disconnect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
package me.zhengjie.utils

import me.zhengjie.utils.FileUtil.getExtensionName
import me.zhengjie.utils.FileUtil.getFileNameNoEx
import me.zhengjie.utils.FileUtil.getSize
import me.zhengjie.utils.FileUtil.toFile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile

class FileUtilTest {
    @Test
    fun testToFile() {
        val retval = toFile(MockMultipartFile("foo", null as ByteArray?))!!.totalSpace
        Assertions.assertEquals(500695072768L, retval)
    }

    @Test
    fun testGetExtensionName() {
        Assertions.assertEquals("foo", getExtensionName("foo"))
        Assertions.assertEquals("exe", getExtensionName("bar.exe"))
    }

    @Test
    fun testGetFileNameNoEx() {
        Assertions.assertEquals("foo", getFileNameNoEx("foo"))
        Assertions.assertEquals("bar", getFileNameNoEx("bar.txt"))
    }

    @Test
    fun testGetSize() {
        Assertions.assertEquals("1000B   ", getSize(1000))
        Assertions.assertEquals("1.00KB   ", getSize(1024))
        Assertions.assertEquals("1.00MB   ", getSize(1048576))
        Assertions.assertEquals("1.00GB   ", getSize(1073741824))
    }
}
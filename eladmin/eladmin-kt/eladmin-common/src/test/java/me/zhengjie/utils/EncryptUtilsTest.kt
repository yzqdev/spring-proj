package me.zhengjie.utils

import me.zhengjie.utils.EncryptUtils.desDecrypt
import me.zhengjie.utils.EncryptUtils.desEncrypt
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EncryptUtilsTest {
    /**
     * 对称加密
     */
    @Test
    fun testDesEncrypt() {
        try {
            Assertions.assertEquals("7772841DC6099402", desEncrypt("123456"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 对称解密
     */
    @Test
    fun testDesDecrypt() {
        try {
            Assertions.assertEquals("123456", desDecrypt("7772841DC6099402"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
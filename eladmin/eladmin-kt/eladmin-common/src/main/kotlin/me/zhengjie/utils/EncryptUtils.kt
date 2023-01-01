/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.utils

import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec

/**
 * 加密
 * @author Zheng Jie
 * @date 2018-11-23
 */
object EncryptUtils {
    private const val STR_PARAM = "Passw0rd"
    private var cipher: Cipher? = null
    private val IV = IvParameterSpec(STR_PARAM.toByteArray(StandardCharsets.UTF_8))
    @Throws(Exception::class)
    private fun getDesKeySpec(source: String?): DESKeySpec? {
        if (source == null || source.length == 0) {
            return null
        }
        cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
        val strKey = "Passw0rd"
        return DESKeySpec(strKey.toByteArray(StandardCharsets.UTF_8))
    }

    /**
     * 对称加密
     */
    @JvmStatic
    @Throws(Exception::class)
    fun desEncrypt(source: String): String {
        val desKeySpec = getDesKeySpec(source)
        val keyFactory = SecretKeyFactory.getInstance("DES")
        val secretKey = keyFactory.generateSecret(desKeySpec)
        cipher!!.init(Cipher.ENCRYPT_MODE, secretKey, IV)
        return byte2hex(
            cipher!!.doFinal(source.toByteArray(StandardCharsets.UTF_8))
        ).uppercase(Locale.getDefault())
    }

    /**
     * 对称解密
     */
    @JvmStatic
    @Throws(Exception::class)
    fun desDecrypt(source: String): String {
        val src = hex2byte(source.toByteArray(StandardCharsets.UTF_8))
        val desKeySpec = getDesKeySpec(source)
        val keyFactory = SecretKeyFactory.getInstance("DES")
        val secretKey = keyFactory.generateSecret(desKeySpec)
        cipher!!.init(Cipher.DECRYPT_MODE, secretKey, IV)
        val retByte = cipher!!.doFinal(src)
        return String(retByte)
    }

    private fun byte2hex(inStr: ByteArray): String {
        var stmp: String
        val out = StringBuilder(inStr.size * 2)
        for (b in inStr) {
            stmp = Integer.toHexString(b.toInt() and 0xFF)
            if (stmp.length == 1) {
                // 如果是0至F的单位字符串，则添加0
                out.append("0").append(stmp)
            } else {
                out.append(stmp)
            }
        }
        return out.toString()
    }

    private fun hex2byte(b: ByteArray): ByteArray {
        val size = 2
        require(b.size % size == 0) { "长度不是偶数" }
        val b2 = ByteArray(b.size / 2)
        var n = 0
        while (n < b.size) {
            val item = String(b, n, 2)
            b2[n / 2] = item.toInt(16).toByte()
            n += size
        }
        return b2
    }
}
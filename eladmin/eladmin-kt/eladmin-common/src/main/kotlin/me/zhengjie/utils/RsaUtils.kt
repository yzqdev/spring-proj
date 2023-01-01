package me.zhengjie.utils

import org.apache.commons.codec.binary.Base64
import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * @author https://www.cnblogs.com/nihaorz/p/10690643.html
 * @description Rsa 工具类，公钥私钥生成，加解密
 * @date 2020-05-18
 */
object RsaUtils {
    private const val SRC = "123456"
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        println("\n")
        val keyPair = generateKeyPair()
        println("公钥：" + keyPair.publicKey)
        println("私钥：" + keyPair.privateKey)
        println("\n")
        test1(keyPair)
        println("\n")
        test2(keyPair)
        println("\n")
    }

    /**
     * 公钥加密私钥解密
     */
    @Throws(Exception::class)
    private fun test1(keyPair: RsaKeyPair) {
        println("***************** 公钥加密私钥解密开始 *****************")
        val text1 = encryptByPublicKey(keyPair.publicKey, SRC)
        val text2 = decryptByPrivateKey(keyPair.privateKey, text1)
        println("加密前：" + SRC)
        println("加密后：$text1")
        println("解密后：$text2")
        if (SRC == text2) {
            println("解密字符串和原始字符串一致，解密成功")
        } else {
            println("解密字符串和原始字符串不一致，解密失败")
        }
        println("***************** 公钥加密私钥解密结束 *****************")
    }

    /**
     * 私钥加密公钥解密
     * @throws Exception /
     */
    @Throws(Exception::class)
    private fun test2(keyPair: RsaKeyPair) {
        println("***************** 私钥加密公钥解密开始 *****************")
        val text1 = encryptByPrivateKey(keyPair.privateKey, SRC)
        val text2 = decryptByPublicKey(keyPair.publicKey, text1)
        println("加密前：" + SRC)
        println("加密后：$text1")
        println("解密后：$text2")
        if (SRC == text2) {
            println("解密字符串和原始字符串一致，解密成功")
        } else {
            println("解密字符串和原始字符串不一致，解密失败")
        }
        println("***************** 私钥加密公钥解密结束 *****************")
    }

    /**
     * 公钥解密
     *
     * @param publicKeyText 公钥
     * @param text 待解密的信息
     * @return /
     * @throws Exception /
     */
    @Throws(Exception::class)
    fun decryptByPublicKey(publicKeyText: String?, text: String?): String {
        val x509EncodedKeySpec = X509EncodedKeySpec(Base64.decodeBase64(publicKeyText))
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey = keyFactory.generatePublic(x509EncodedKeySpec)
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, publicKey)
        val result = doLongerCipherFinal(Cipher.DECRYPT_MODE, cipher, Base64.decodeBase64(text))
        return String(result)
    }

    /**
     * 私钥加密
     *
     * @param privateKeyText 私钥
     * @param text 待加密的信息
     * @return /
     * @throws Exception /
     */
    @Throws(Exception::class)
    fun encryptByPrivateKey(privateKeyText: String?, text: String): String {
        val pkcs8EncodedKeySpec = PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText))
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec)
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, privateKey)
        val result = doLongerCipherFinal(Cipher.ENCRYPT_MODE, cipher, text.toByteArray())
        return Base64.encodeBase64String(result)
    }

    /**
     * 私钥解密
     *
     * @param privateKeyText 私钥
     * @param text 待解密的文本
     * @return /
     * @throws Exception /
     */
    @JvmStatic
    @Throws(Exception::class)
    fun decryptByPrivateKey(privateKeyText: String?, text: String?): String {
        val pkcs8EncodedKeySpec5 = PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText))
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5)
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val result = doLongerCipherFinal(Cipher.DECRYPT_MODE, cipher, Base64.decodeBase64(text))
        return String(result)
    }

    /**
     * 公钥加密
     *
     * @param publicKeyText 公钥
     * @param text 待加密的文本
     * @return /
     */
    @Throws(Exception::class)
    fun encryptByPublicKey(publicKeyText: String?, text: String): String {
        val x509EncodedKeySpec2 = X509EncodedKeySpec(Base64.decodeBase64(publicKeyText))
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey = keyFactory.generatePublic(x509EncodedKeySpec2)
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        // 返回UTF-8编码的解密信息
        val result = doLongerCipherFinal(Cipher.ENCRYPT_MODE, cipher, text.toByteArray())
        return Base64.encodeBase64String(result)
    }

    @Throws(Exception::class)
    private fun doLongerCipherFinal(opMode: Int, cipher: Cipher, source: ByteArray): ByteArray {
        val out = ByteArrayOutputStream()
        if (opMode == Cipher.DECRYPT_MODE) {
            out.write(cipher.doFinal(source))
        } else {
            var offset = 0
            val totalSize = source.size
            // 对数据分段解密
            while (totalSize - offset > 0) {
                val size = Math.min(cipher.getOutputSize(0) - 11, totalSize - offset)
                out.write(cipher.doFinal(source, offset, size))
                offset += size
            }
        }
        out.close()
        return out.toByteArray()
    }

    /**
     * 构建RSA密钥对
     *
     * @return /
     * @throws NoSuchAlgorithmException /
     */
    @Throws(NoSuchAlgorithmException::class)
    fun generateKeyPair(): RsaKeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(1024)
        val keyPair = keyPairGenerator.generateKeyPair()
        val rsaPublicKey = keyPair.public as RSAPublicKey
        val rsaPrivateKey = keyPair.private as RSAPrivateKey
        val publicKeyString = Base64.encodeBase64String(rsaPublicKey.encoded)
        val privateKeyString = Base64.encodeBase64String(rsaPrivateKey.encoded)
        return RsaKeyPair(publicKeyString, privateKeyString)
    }

    /**
     * RSA密钥对对象
     */
    class RsaKeyPair(val publicKey: String, val privateKey: String)
}
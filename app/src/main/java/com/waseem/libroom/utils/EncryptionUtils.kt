package com.waseem.libroom.utils

import io.ktor.util.hex
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object EncryptionUtils {
    private const val SECRET_KEY = "Hzxyd123Hzxyd123"
    private const val IV = "HzxydXInitVector"

    fun encryptPassword(password: String): String {
        val timestamp = System.currentTimeMillis() / 1000
        val data = "$timestamp:$password"
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(IV.toByteArray())
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        val encrypted = cipher.doFinal(data.toByteArray())
        return encrypted.toHex()
    }

    private fun ByteArray.toHex(): String {
        return joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }
    }
}
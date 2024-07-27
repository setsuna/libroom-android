package com.waseem.libroom.utils

import android.media.MediaDrm
import android.os.Build
import com.waseem.libroom.feature.root.device.DeviceType
import java.security.MessageDigest
import java.util.*
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object EncryptionUtils {
    private const val SECRET_KEY = "Hzxyd123Hzxyd123"
    private const val IV = "HzxydXInitVector"

    private fun encryptData(data: String): String {
        val secretKey = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(IV.toByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        val encryptedBytes = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    private fun stringToHex(str: String): String {
        return str.toByteArray().joinToString("") { "%02x".format(it) }
    }

    fun encryptPassword(password: String): String {
        val timestamp = (System.currentTimeMillis() / 1000).toInt()
        val newPassword = stringToHex(encryptData("$timestamp:$password"))
        return newPassword
    }

    /** widevineId, 通过安卓数字版权管理(DRM)框架获取的唯一设备ID */
    fun getWidevineId(): String {
        var drm: MediaDrm? = null
        try {
            val widevineUuid = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
            drm = MediaDrm(widevineUuid)
            return drm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID).hash()
        } catch (ex: Throwable) {
            //
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                drm?.close()
            } else {
                drm?.release()
            }
        }
        return ""
    }

    private fun ByteArray.hash(algo: String = "sha1"): String {
        val data = MessageDigest.getInstance(algo).digest(this)
        val hex = StringBuilder()
        for (b in data) {
            if (b.toInt() and 0xff < 0x10) {
                hex.append("0")
            }
            hex.append(Integer.toHexString(b.toInt() and 0xff))
        }
        return hex.toString()
    }

    fun getDeviceType(): DeviceType {
        // 根据设备特性判断类型，这里仅为示例
        return DeviceType.MOBILE_CLIENT
    }
}
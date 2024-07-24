package com.waseem.libroom.utils

import android.media.MediaDrm
import android.os.Build
import com.waseem.libroom.feature.root.device.DeviceType
import com.waseem.libroom.utils.EncryptionUtils.toHexString
import io.ktor.util.hex
import java.util.UUID
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
    fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

    fun getUniqueDeviceCode(): String {
        val WIDEVINE_UUID = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
        return try {
            var mediaDrm: MediaDrm? = null
            try {
                mediaDrm = MediaDrm(WIDEVINE_UUID)
                val widevineId = mediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
                widevineId.toHexString()
            } finally {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    mediaDrm?.close()
                } else {
                    @Suppress("DEPRECATION")
                    mediaDrm?.release()
                }
            }
        } catch (e: Exception) {
            // 如果获取 DRM ID 失败，可以回退到其他方法
            return "000"
        }
    }
    fun getDeviceType(): DeviceType {
        // 根据设备特性判断类型，这里仅为示例
        return DeviceType.CLIENT_DESKCARD
    }
}
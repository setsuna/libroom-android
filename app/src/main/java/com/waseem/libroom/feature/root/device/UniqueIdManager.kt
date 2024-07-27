package com.waseem.libroom.feature.root.device

import android.content.Context
import android.provider.Settings
import com.waseem.libroom.utils.EncryptionUtils
import java.security.MessageDigest
import java.util.UUID

class UniqueIdManager(private val context: Context) {

    fun getUniqueId(): String {
        var androidId = EncryptionUtils.getWidevineId()
        if(androidId.isEmpty()) {
            androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            return androidId ?: UUID.randomUUID().toString().toByteArray().hash()
        }else
            return androidId
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
}
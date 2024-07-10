package com.waseem.libroom.utils

import java.security.MessageDigest

object EncryptionUtils {
    fun md5(input: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
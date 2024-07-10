package com.waseem.libroom.core.token

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userSettings by preferencesDataStore("user_settings")

@Singleton
class TokenManager @Inject constructor(private val context: Context){
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val TOKEN_TD = stringPreferencesKey("td_token")
    }

    val token: Flow<String?> = context.userSettings.data.map { preferences ->
        preferences[TOKEN_KEY]
        preferences[TOKEN_TD]
    }

    suspend fun saveToken(token: String, token_td : String) {
        context.userSettings.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[TOKEN_TD] = token_td
        }
    }

    suspend fun deleteToken() {
        context.userSettings.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(TOKEN_TD)
        }
    }
}
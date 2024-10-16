package com.devjsg.cj_logistics_future_technology.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("user_prefs")

class DataStoreManager @Inject constructor(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val NAME_KEY = stringPreferencesKey("name")
        val HEART_RATE_THRESHOLD_KEY = intPreferencesKey("heart_rate_threshold")
    }

    val token: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    val refreshToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN_KEY]
    }

    val name: Flow<String?> = dataStore.data.map { preferences ->
        preferences[NAME_KEY]
    }

    val heartRateThreshold: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[HEART_RATE_THRESHOLD_KEY]
    }

    suspend fun saveToken(token: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun saveUserInfo(name: String, heartRateThreshold: Int) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
            preferences[HEART_RATE_THRESHOLD_KEY] = heartRateThreshold
        }
    }

    suspend fun saveHeartRateThreshold(heartRateThreshold: Int) {
        dataStore.edit { preferences ->
            preferences[HEART_RATE_THRESHOLD_KEY] = heartRateThreshold
        }
    }

    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }

    suspend fun clearHeaderData() {
        dataStore.edit { preferences ->
            preferences.remove(NAME_KEY)
            preferences.remove(HEART_RATE_THRESHOLD_KEY)
        }
    }
}
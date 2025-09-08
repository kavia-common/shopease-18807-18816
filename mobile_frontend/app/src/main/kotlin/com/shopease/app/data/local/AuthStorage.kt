package com.shopease.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth")

/**
 * PUBLIC_INTERFACE
 * AuthStorage
 *
 * Persists auth token using DataStore.
 */
class AuthStorage(private val context: Context) {

    private val KEY_TOKEN = stringPreferencesKey("token")
    private val KEY_NAME = stringPreferencesKey("name")
    private val KEY_EMAIL = stringPreferencesKey("email")

    // PUBLIC_INTERFACE
    fun token(): Flow<String?> = context.dataStore.data.map { it[KEY_TOKEN] }

    // PUBLIC_INTERFACE
    suspend fun setSession(name: String, email: String, token: String?) {
        context.dataStore.edit {
            it[KEY_NAME] = name
            it[KEY_EMAIL] = email
            token?.let { t -> it[KEY_TOKEN] = t }
        }
    }

    // PUBLIC_INTERFACE
    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}

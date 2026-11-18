package com.example.scoresapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    private object Keys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val FAVORITES_ONLY = booleanPreferencesKey("favorites_only")
    }

    /** Falls back to empty preferences instead of crashing if the file is unreadable. */
    private val preferences: Flow<Preferences> =
        context.dataStore.data.catch { throwable ->
            if (throwable is IOException) emit(emptyPreferences()) else throw throwable
        }

    val darkMode: Flow<Boolean> =
        preferences.map { it[Keys.DARK_MODE] ?: false }

    val favoritesOnly: Flow<Boolean> =
        preferences.map { it[Keys.FAVORITES_ONLY] ?: false }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[Keys.DARK_MODE] = enabled }
    }

    suspend fun setFavoritesOnly(enabled: Boolean) {
        context.dataStore.edit { it[Keys.FAVORITES_ONLY] = enabled }
    }
}



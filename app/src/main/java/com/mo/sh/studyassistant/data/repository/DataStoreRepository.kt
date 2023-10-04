package com.mo.sh.studyassistant.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.mo.sh.studyassistant.domain.repository.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private val Context.dataStore by preferencesDataStore(name = "preferences")

class DataStoreRepository(
    private val context: Context
): PreferencesRepository {

    override suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { settings ->
                if (settings[key] != value)
                    settings[key] = value
            }
        }
    }

    override fun <T> get(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return context.dataStore.data.map { preferences -> preferences[key] ?: defaultValue }
    }

    companion object {
        const val API_KEY = "api_key"
        const val THEME = "theme"


        const val THEME_LIGHT = 0
        const val THEME_DARK = 1
        const val THEME_SYSTEM = 2
    }

}
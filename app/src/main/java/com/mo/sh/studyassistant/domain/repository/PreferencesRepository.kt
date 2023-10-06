package com.mo.sh.studyassistant.domain.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun <T> save(key: Preferences.Key<T>, value: T)

    fun <T> get(key: Preferences.Key<T>, defaultValue: T): Flow<T>
}
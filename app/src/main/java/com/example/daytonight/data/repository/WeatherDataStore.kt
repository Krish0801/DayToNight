package com.example.daytonight.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.daytonight.data.model.TempMeasure
import com.example.daytonight.data.model.StoredPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherDataStore(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("WEATHER")

    companion object {
        val LAST_SEARCHED_LOCATION = stringPreferencesKey("LAST_SEARCHED_LOCATION")
        val PREFERRED_TEMP_MEASURE = stringPreferencesKey("PREFERRED_TEMP_MEASURE")
    }

    private val dataStore = context.dataStore

    suspend fun updateLastSearchedLocation(searchedLocation: String) {
        dataStore.edit { preferences ->
            preferences[LAST_SEARCHED_LOCATION] = searchedLocation
        }
    }


    fun getStoredPreferences(): Flow<StoredPreferences> =
        dataStore.data.map { preferences ->
            val lastSearchedLocation = preferences[LAST_SEARCHED_LOCATION] ?: ""
            val preferredTempMeasure =
                preferences[PREFERRED_TEMP_MEASURE]?.let { TempMeasure.valueOf(it) }
                    ?: TempMeasure.Celcius
            StoredPreferences(lastSearchedLocation, preferredTempMeasure)
        }
}

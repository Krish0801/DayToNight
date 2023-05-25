package com.example.daytonight.data.model

data class StoredPreferences(val lastStoredLocation: String? = null, val tempMeasure: TempMeasure = TempMeasure.Celcius)
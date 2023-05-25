package com.example.daytonight.data.repository

import com.example.daytonight.data.model.WeatherCityModel
import com.example.daytonight.data.remote.ApiRequest
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiRequest: ApiRequest
) : Repository {

    override suspend fun getWeather(inputSearchString: String): WeatherCityModel {
        return apiRequest.getWeather(inputSearchString)
    }

    override suspend fun getWeather(latitude: Double, longitude: Double): WeatherCityModel {
        return apiRequest.getWeather(latitude, longitude)
    }
}
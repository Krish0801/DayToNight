package com.example.daytonight.data.repository

import com.example.daytonight.data.model.WeatherCityModel


interface Repository {

    suspend fun getWeather(inputSearchString: String): WeatherCityModel

    suspend fun getWeather(latitude: Double, longitude: Double): WeatherCityModel
}
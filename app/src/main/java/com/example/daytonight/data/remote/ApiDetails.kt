package com.example.daytonight.data.remote

object ApiDetails {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val WEATHER_URL = "weather"
    const val APP_ID = "7c9029dde5011ec62a103f98038e7ffc"
    const val WEATHER_ICON = "https://openweathermap.org/img/wn/{icon_id}@2x.png"
    fun icon(icon: String) = WEATHER_ICON.replace("{icon_id}", icon)
}

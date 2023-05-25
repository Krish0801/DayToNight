package com.example.daytonight

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.daytonight.data.repository.Repository
import com.example.daytonight.data.repository.WeatherDataStore
import com.example.daytonight.ui.screen.HomeScreen
import com.example.daytonight.ui.screen.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val weatherDataStore = WeatherDataStore(this)

        setContent {
            HomeScreen(weatherDataStore)
        }
    }
}

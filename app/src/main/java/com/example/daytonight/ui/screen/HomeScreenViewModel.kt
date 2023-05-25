package com.example.daytonight.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytonight.data.model.GeoModel
import com.example.daytonight.data.model.WeatherCityModel
import com.example.daytonight.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
     val repository: Repository
) : ViewModel() {

    //Stateflow has to be initialized with the default/initial value
    private val _weatherInfo = MutableStateFlow(WeatherCityModel())
    val weatherInfo: StateFlow<WeatherCityModel> = _weatherInfo

    private val _currentLocationInfo = MutableStateFlow(WeatherCityModel())
    val currentLocationInfo: StateFlow<WeatherCityModel> = _currentLocationInfo

    private val _reportError: MutableStateFlow<String> = MutableStateFlow("")
    val reportError : StateFlow<String> = _reportError

    fun loadWeatherDetails(searchCity: String, onSuccess: suspend (String) -> Unit) {
        viewModelScope.launch {
            try {
                _weatherInfo.value = repository.getWeather(searchCity)
                onSuccess(searchCity)
            } catch (e: Exception) {
                e.printStackTrace()
                _reportError.value = "Unable to get details for $searchCity: ${e.message}"
            }
        }
    }

    fun loadWeatherDetails(geoModel: GeoModel) {
        geoModel.lat?.let { lat ->
            geoModel.lon?.let { lon ->
                viewModelScope.launch {
                    try {
                        _currentLocationInfo.value = repository.getWeather(lat, lon)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        _reportError.value = "Unable to get details for $geoModel: ${e.message}"
                    }
                }
            }
        }
    }

    fun clearError() {
        _reportError.value = ""
    }
}

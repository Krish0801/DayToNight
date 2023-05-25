package com.example.daytonight.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.daytonight.data.model.GeoModel
import com.example.daytonight.data.model.StoredPreferences
import com.example.daytonight.data.model.TempMeasure
import com.example.daytonight.data.model.WeatherCityModel
import com.example.daytonight.data.remote.ApiDetails
import com.example.daytonight.data.repository.WeatherDataStore
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun HomeScreen(
    weatherDataStore: WeatherDataStore
) {
    val viewModel: HomeScreenViewModel = hiltViewModel()

    val weatherDetails by viewModel.weatherInfo.collectAsState(initial = WeatherCityModel())
    val currentLocDetails by viewModel.currentLocationInfo.collectAsState(initial = WeatherCityModel())
    val reportError by viewModel.reportError.collectAsState(initial = "")

    // Check and display if there is any error to be reported
    displayError(reportError, LocalContext.current) { viewModel.clearError() }

    val storedPreferences = weatherDataStore.getStoredPreferences().collectAsState(initial = StoredPreferences())
    val weatherSelectionState = remember { mutableStateOf(storedPreferences.value.tempMeasure) }

    LaunchedEffect(storedPreferences.value.lastStoredLocation) {
        storedPreferences.value.lastStoredLocation?.let { lastLocation ->
            viewModel.loadWeatherDetails(lastLocation) { searchString ->
                weatherDataStore.updateLastSearchedLocation(searchString)
            }
        }
    }

    val searchState = remember { mutableStateOf(TextFieldValue(storedPreferences.value.lastStoredLocation ?: "")) }
    val loadWeatherForSearch: (String) -> Unit =
        { searchString ->
            viewModel.loadWeatherDetails(searchString) {searchString -> weatherDataStore.updateLastSearchedLocation(searchString)}
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFEEB281))
    ) {
        val locationGeoModel = remember { mutableStateOf(GeoModel()) }
        viewModel.loadWeatherDetails(locationGeoModel.value)

        Permission(locationGeoModel = locationGeoModel)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEEB281))
                .padding(horizontal = 10.dp)
        ) {
            SearchBox(searchState, loadWeatherForSearch)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            if (weatherDetails.name!!.isNotBlank()) {
                HomeScreenContent(weatherDetails, weatherSelectionState)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            if (currentLocDetails.name!!.isNotBlank()) {
                HomeScreenContent(currentLocDetails, weatherSelectionState)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HomeScreenContent(weatherDetails: WeatherCityModel, weatherSelectionState: MutableState<TempMeasure>) {
    val tempMeasure = weatherSelectionState.value

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = weatherDetails.name?.let { "$it (${weatherDetails.sys!!.country})" } ?: "",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = temperatureConvert(weatherDetails.main!!.temp, tempMeasure),
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            LazyRow(
                modifier = Modifier.align(Alignment.Center),
                content = {
                    itemsIndexed(weatherDetails.weather ?: emptyList()) { _, item ->
                        item?.let {
                            Box(
                                modifier = Modifier
                                    .size(160.dp)
                                    .fillMaxHeight()
                            ) {
                                AsyncImage(
                                    model = ApiDetails.icon(it.icon!!),
                                    contentDescription = it.description,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Timezone: ${displayTimeZone(weatherDetails.timezone!!)}",
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${temperatureConvert(weatherDetails.main!!.tempMax, tempMeasure)}/${
                temperatureConvert(
                weatherDetails.main!!.tempMin,
                tempMeasure
            )
            }, Feels like ${temperatureConvert(weatherDetails.main!!.feelsLike, tempMeasure)}",
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Air Pressure: ${(weatherDetails.main!!.pressure).toString()} mb",
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Humidity: ${(weatherDetails.main!!.humidity).toString()}%",
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Visibility: ${(weatherDetails.visibility!! / 1000)} km",
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Sunrise: ${displayTime(weatherDetails.sys!!.sunrise!!.toLong())}",
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Sunset: ${displayTime(weatherDetails.sys!!.sunset!!.toLong())}",
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Wind Speed:  ${(weatherDetails.wind!!.speed)} km in ${weatherDetails.wind!!.deg}°",
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun displayTimeZone(timeZone: Int): String {
    val fromGmt = timeZone.toDouble() / (60 * 60)
    return "${if (fromGmt > 0) "GMT +" else "GMT -"} ${Math.abs(fromGmt)}"
}

@RequiresApi(Build.VERSION_CODES.O)
fun displayTime(time: Long): String =
    LocalDateTime.ofInstant(
        Instant.ofEpochMilli(time * 1000),
        TimeZone.getDefault().toZoneId()
    ).format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss"))

fun temperatureConvert(temp: Double?, tempMeasure: TempMeasure): String {
    val defaultKelvin = 273.15
    val finalTemp = temp ?: defaultKelvin
    return when (tempMeasure) {
        TempMeasure.Celcius -> (finalTemp - defaultKelvin).roundToInt().toString() + "\u00B0 C"
        TempMeasure.Fahrenheit -> ((finalTemp - defaultKelvin) * (9 / 5) + 32).roundToInt().toString() + "\u00B0 F"
        TempMeasure.Kelvin -> finalTemp.roundToInt().toString() + "\u00B0 K"
    }
}


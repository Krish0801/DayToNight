package com.example.daytonight.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.example.daytonight.data.model.GeoModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@Composable
fun Permission(locationGeoModel: MutableState<GeoModel>) {
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val locationPermissionGranted = remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted.value = isGranted
        if (!isGranted) {
            displayError("Cannot display weather from the current location. Location permission denied.", context) {}
        }
    }

    val requestLocationUpdatesLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        locationGeoModel.value = GeoModel(location.latitude, location.longitude)
                    }
                }
        } else {
            displayError("Cannot display weather from the current location. Location permission denied.", context) {}
        }
    }

    SideEffect {
        if (!locationPermissionGranted.value) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            if (isLocationPermissionGranted(context)) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            locationGeoModel.value = GeoModel(location.latitude, location.longitude)
                        }
                    }
            } else {
                requestLocationUpdatesLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}

private fun isLocationPermissionGranted(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun displayError(message: String, context: Context, action: () -> Unit) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    action()
}

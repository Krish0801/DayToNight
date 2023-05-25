package com.example.daytonight

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashScreen {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

    }
}

@Composable
fun SplashScreen(navigateTo: () -> Unit) {

    val splashColor =  Color(0xFFEEB281)
    Surface(color = splashColor) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Add your splash screen content here, such as a logo or image
            Image(
                painter = painterResource(id = R.drawable.ic_weather),
                contentDescription = null,
                modifier = Modifier.size(250.dp)
            )
        }

        LaunchedEffect(Unit) {
            // Simulate a delay for the splash screen
            delay(1000)

            // Navigate to the next activity
            navigateTo()
        }
    }
}
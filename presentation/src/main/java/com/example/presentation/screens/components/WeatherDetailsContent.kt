package com.example.presentation.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.presentation.viewmodel.utils.WeatherDisplayData

@Composable
fun WeatherDetailsContent(
    weatherData: WeatherDisplayData,
    showMainInfo: Boolean = true,
    showCurrentDetails: Boolean = true,
    showFullDetails: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        if (showMainInfo) {
            Text(text = weatherData.locationName, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = weatherData.iconUrl,
                contentDescription = weatherData.description,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = weatherData.temperature, style = MaterialTheme.typography.displayLarge)
            Text(text = weatherData.description, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (showCurrentDetails) {
            Text("Current Details", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = weatherData.minTemperature, style = MaterialTheme.typography.bodyLarge)
                Text(text = weatherData.maxTemperature, style = MaterialTheme.typography.bodyLarge)
                Text(text = weatherData.feelsLikeTemperature, style = MaterialTheme.typography.bodyLarge)
                if (showFullDetails) {
                    Text(text = weatherData.humidity, style = MaterialTheme.typography.bodyLarge)
                    Text(text = weatherData.windSpeed, style = MaterialTheme.typography.bodyLarge)
                    Text(text = weatherData.pressure, style = MaterialTheme.typography.bodyLarge)
                    Text(text = weatherData.visibility, style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Sunrise: ${weatherData.sunriseTime}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Sunset: ${weatherData.sunsetTime}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

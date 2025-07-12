package com.example.presentation.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.model.WeatherDisplayData

@Composable
fun WeatherDetails(
    weatherData: WeatherDisplayData,
    modifier: Modifier = Modifier,
    showMainInfo: Boolean = true,
    showCurrentDetails: Boolean = true,
    showFullDetails: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showCurrentDetails) {
            Text("Current Details", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                IconText(Icons.Rounded.KeyboardArrowDown, weatherData.minTemperature)
                IconText(Icons.Rounded.KeyboardArrowUp, weatherData.maxTemperature)

                if (showFullDetails) {
                    IconText(Icons.Rounded.WaterDrop, weatherData.humidity)
                    IconText(Icons.Rounded.Air, weatherData.windSpeed)
                    IconText(Icons.Rounded.Speed, weatherData.pressure)
                    IconText(Icons.Rounded.Visibility, weatherData.visibility)
                    IconText(Icons.Rounded.WbSunny, weatherData.sunriseTime)
                    IconText(Icons.Rounded.NightsStay, weatherData.sunsetTime)
                }
            }
        }
    }
}

@Composable
private fun IconText(icon: ImageVector, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(12.dp))
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
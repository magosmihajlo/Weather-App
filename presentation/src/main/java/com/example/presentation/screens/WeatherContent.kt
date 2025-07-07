package com.example.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.presentation.uimodel.WeatherDisplayData

@Composable
fun WeatherContent(weatherData: WeatherDisplayData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
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

        Text("Current Details", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(text = weatherData.minTemperature, style = MaterialTheme.typography.bodyLarge)
            Text(text = weatherData.maxTemperature, style = MaterialTheme.typography.bodyLarge)
            Text(text = weatherData.feelsLikeTemperature, style = MaterialTheme.typography.bodyLarge)
            Text(text = weatherData.humidity, style = MaterialTheme.typography.bodyLarge)
            Text(text = weatherData.windSpeed, style = MaterialTheme.typography.bodyLarge)
            Text(text = weatherData.pressure, style = MaterialTheme.typography.bodyLarge)
            Text(text = weatherData.visibility, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Sunrise: ${weatherData.sunriseTime}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Sunset: ${weatherData.sunsetTime}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultWeatherContentPreview() {
    val dummyData = WeatherDisplayData(
        locationName = "Novi Sad",
        description = "Clear Sky",
        iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
        temperature = "28.5°C",
        minTemperature = "Min: 22.0°C",
        maxTemperature = "Max: 30.0°C",
        feelsLikeTemperature = "Feels Like: 27.0°C",
        humidity = "65%",
        windSpeed = "15.2 km/h",
        pressure = "1010 hPa",
        visibility = "10 km",
        sunriseTime = "05:30 AM",
        sunsetTime = "08:30 PM"
    )
    WeatherContent(dummyData)
}
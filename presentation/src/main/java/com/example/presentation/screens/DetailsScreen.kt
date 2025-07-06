package com.example.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.presentation.uimodel.WeatherDisplayData
import com.example.presentation.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val weatherData by viewModel.weatherDisplayData.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detailed Weather") },

            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else if (errorMessage != null) {
                    Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                } else if (weatherData != null) {
                    DetailedWeatherContent(weatherData!!)
                } else {
                    Text("No detailed weather data available. Please check the main screen.")
                }
            }
        }
    )
}

@Composable
fun DetailedWeatherContent(weatherData: WeatherDisplayData) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = weatherData.locationName, style = MaterialTheme.typography.headlineLarge)
        Text(text = weatherData.description, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        AsyncImage(
            model = weatherData.iconUrl,
            contentDescription = weatherData.description,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = weatherData.temperature, style = MaterialTheme.typography.displayMedium)
        Text(text = weatherData.feelsLikeTemperature, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = weatherData.minTemperature, style = MaterialTheme.typography.bodyLarge)
            Text(text = weatherData.maxTemperature, style = MaterialTheme.typography.bodyLarge)
            Text(text = weatherData.humidity, style = MaterialTheme.typography.bodyLarge)
            Text(text = weatherData.windSpeed, style = MaterialTheme.typography.bodyLarge)
            Text(text = weatherData.pressure, style = MaterialTheme.typography.bodyLarge)
            Text(text = weatherData.visibility, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Sunrise: ${weatherData.sunriseTime}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Sunset: ${weatherData.sunsetTime}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
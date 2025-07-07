package com.example.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.presentation.state.WeatherUiState
import com.example.presentation.uimodel.WeatherDisplayData
import com.example.presentation.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState: WeatherUiState<WeatherDisplayData> by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detailed Weather") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when (uiState) {
                    is WeatherUiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is WeatherUiState.Error -> {
                        Text(
                            text = (uiState as WeatherUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    is WeatherUiState.Success -> {
                        DetailedWeatherContent((uiState as WeatherUiState.Success).data)
                    }
                    WeatherUiState.Empty -> {
                        Text("No detailed weather data available. Please check the main screen.")
                    }
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

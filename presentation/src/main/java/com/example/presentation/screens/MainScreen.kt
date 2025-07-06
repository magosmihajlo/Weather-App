package com.example.presentation.screens

// TODO: I put too much stuff in one place. Separate code and check for possible reusability improvements
// TODO: Presentation layer is pretty basic for now, much improvement needed

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.presentation.uimodel.WeatherDisplayData
import com.example.presentation.viewmodel.WeatherViewModel
import com.example.presentation.navigation.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val weatherData by viewModel.weatherDisplayData.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val currentCity by viewModel.currentCity.collectAsStateWithLifecycle()
    val requestLocationPermission by viewModel.requestLocationPermission.collectAsStateWithLifecycle()

    var cityInput by remember(currentCity) { mutableStateOf(currentCity) }

    val keyboardController = LocalSoftwareKeyboardController.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            viewModel.fetchCurrentLocationWeather()
        } else {
            viewModel.searchWeather("Beograd")
        }
    }

    LaunchedEffect(requestLocationPermission) {
        if (requestLocationPermission) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather App") },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(AppScreen.SettingsScreen.route) {
                                popUpTo(navController.graph.startDestinationRoute!!) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = cityInput,
                        onValueChange = { cityInput = it },
                        label = { Text("Enter City") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                viewModel.searchWeather(cityInput)
                                keyboardController?.hide()
                            }
                        )
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    IconButton(onClick = {
                        viewModel.searchWeather(cityInput)
                        keyboardController?.hide()
                    }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search city")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else if (errorMessage != null) {
                        Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                    } else if (weatherData != null) {
                        WeatherContent(weatherData!!)
                    } else {
                        Text("Enter a city to get weather information.")
                    }
                }

                if (!isLoading && errorMessage == null && weatherData != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate(AppScreen.DetailsScreen.route) {
                                popUpTo(navController.graph.startDestinationRoute!!) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("More Details")
                    }
                }
            }
        }
    )
}

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
fun DefaultMainScreenPreview() {
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
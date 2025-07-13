package com.example.presentation.screens

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.presentation.navigation.AppScreen
import com.example.presentation.navigation.navigateWithState
import com.example.presentation.screens.components.*
import com.example.presentation.state.LocationState
import com.example.presentation.state.WeatherUiState
import com.example.presentation.viewmodel.WeatherViewModel
import com.example.presentation.viewmodel.LocationViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hourlyDisplay by viewModel.hourlyDisplayState.collectAsStateWithLifecycle()
    val dailyDisplay by viewModel.dailyDisplayState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }
        locationViewModel.onPermissionResult(granted)
    }

    LaunchedEffect(locationViewModel) {
        locationViewModel.permissionRequestFlow.onEach {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }.launchIn(this)

        locationViewModel.locationResultFlow.onEach { result ->
            when (result) {
                is LocationState.Success -> viewModel.searchWeather(result.cityName)
                is LocationState.Error -> Log.e("MainScreen", result.message)
                is LocationState.Loading -> { }
            }
        }.launchIn(this)
    }


    AppScaffold(
        navController = navController,
        title = "Weather App"
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                CitySearchBar(onSearch = viewModel::searchWeather)
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                ScreenStateLayout(
                    uiState = uiState,
                    modifier = Modifier.fillMaxWidth()
                ) { weatherData ->
                    WeatherDetails(
                        weatherData = weatherData,
                        showMainInfo = true,
                        showCurrentDetails = false
                    )
                }
            }

            if (uiState is WeatherUiState.Success) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigateWithState(AppScreen.DetailsScreen.route)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("More Details")
                    }
                }
            }

            if (hourlyDisplay.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Hourly Forecast", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    HourlyForecast(hourly = hourlyDisplay)
                }
            }

            if (dailyDisplay.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("6-Day Forecast", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    DailyForecast(daily = dailyDisplay)
                }
            }
        }
    }
}
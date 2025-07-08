package com.example.presentation.screens

import android.Manifest
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
import com.example.presentation.state.WeatherUiState
import com.example.presentation.viewmodel.WeatherViewModel
import com.example.presentation.viewmodel.utils.WeatherDisplayData

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState: WeatherUiState<WeatherDisplayData> by viewModel.uiState.collectAsStateWithLifecycle()
    val requestLocationPermission by viewModel.requestLocationPermission.collectAsStateWithLifecycle()
    val forecast by viewModel.forecastState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        viewModel.onLocationPermissionResult(granted)
    }

    LaunchedEffect(requestLocationPermission) {
        if (requestLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    WeatherScaffold(
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
                WeatherContentStateView(
                    uiState = uiState,
                    modifier = Modifier.fillMaxWidth(),
                    showMainInfo = true
                )
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

            forecast?.let { forecast ->
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Hourly Forecast", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    HourlyForecastRow(forecast.hourly)
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("6-Day Forecast", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    DailyForecastColumn(forecast.daily)
                }
            }
        }
    }
}

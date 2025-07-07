package com.example.presentation.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.presentation.navigation.AppScreen
import com.example.presentation.state.WeatherUiState
import com.example.presentation.uimodel.WeatherDisplayData
import com.example.presentation.viewmodel.WeatherViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState: WeatherUiState<WeatherDisplayData> by viewModel.uiState.collectAsStateWithLifecycle()

    val requestLocationPermission by viewModel.requestLocationPermission.collectAsStateWithLifecycle()

    var cityInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            viewModel.fetchCurrentLocationWeather()
        } else {
            viewModel.searchWeather("Belgrade")
        }
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather App") },
                actions = {
                    IconButton(onClick = { navController.navigate(AppScreen.CitiesScreen.route) }) {
                        Icon(Icons.Filled.Search, contentDescription = "Recent Cities")
                    }
                    IconButton(onClick = { navController.navigate(AppScreen.SettingsScreen.route) }) {
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
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            viewModel.searchWeather(cityInput)
                            keyboardController?.hide()
                        })
                    )
                    Spacer(modifier = Modifier.width(8.dp))
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
                    when (uiState) {
                        is WeatherUiState.Loading -> CircularProgressIndicator()
                        is WeatherUiState.Success -> {
                            val weatherData = (uiState as WeatherUiState.Success<WeatherDisplayData>).data
                            WeatherContent(weatherData = weatherData)
                        }
                        is WeatherUiState.Error -> {
                            val message = (uiState as WeatherUiState.Error).message
                            Text(message, color = MaterialTheme.colorScheme.error)
                        }
                        is WeatherUiState.Empty -> {
                            Text("Enter a city to get weather information.")
                        }
                    }
                }

                if (uiState is WeatherUiState.Success) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate(AppScreen.DetailsScreen.route) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("More Details")
                    }
                }
            }
        }
    )
}
package com.example.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.presentation.navigation.AppScreen
import com.example.presentation.navigation.navigateWithState
import com.example.presentation.screens.components.CityWeatherCard
import com.example.presentation.screens.components.StatefulScreenContent
import com.example.presentation.screens.components.WeatherScaffold
import com.example.presentation.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.recentCitiesUiState.collectAsStateWithLifecycle()

    WeatherScaffold(
        navController = navController,
        title = "Recent Cities",
        showActions = false
    ) { padding ->
        StatefulScreenContent(
            uiState = uiState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) { cities ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(cities) { city ->
                    CityWeatherCard(weatherData = city) {
                        Log.d("ERROR CHECK", "Clicked on city: ${city.locationName}")
                        viewModel.searchWeather(city.locationName)
                        navController.navigateWithState(AppScreen.MainScreen.route)
                    }
                }
            }
        }
    }
}
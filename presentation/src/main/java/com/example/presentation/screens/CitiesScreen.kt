package com.example.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.presentation.navigation.AppScreen
import com.example.presentation.navigation.navigateWithState
import com.example.presentation.screens.components.AppScaffold
import com.example.presentation.screens.components.CityWeatherCard
import com.example.presentation.screens.components.ScreenStateLayout
import com.example.presentation.viewmodel.CitiesViewModel
import com.example.presentation.viewmodel.WeatherViewModel

@Composable
fun CitiesScreen(
    navController: NavController,
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    citiesViewModel: CitiesViewModel = hiltViewModel()
) {
    val uiState by citiesViewModel.uiState.collectAsStateWithLifecycle()

    AppScaffold(
        navController = navController,
        title = "Recent Cities",
        showActions = false
    ) { padding ->
        ScreenStateLayout(
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
                        weatherViewModel.searchWeather(city.locationName)
                        navController.navigateWithState(AppScreen.MainScreen.route)
                    }
                }
            }
        }
    }
}

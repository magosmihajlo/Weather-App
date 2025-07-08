package com.example.presentation.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.state.WeatherUiState
import com.example.presentation.uimodel.WeatherDisplayData
import com.example.presentation.screens.WeatherContent

@Composable
fun WeatherContentSection(
    uiState: WeatherUiState<WeatherDisplayData>,
    modifier: Modifier = Modifier,
    showMainInfo: Boolean = true,
    showCurrentDetails: Boolean = false,
    showFullDetails: Boolean = false
) {
    StatefulScreenContent(
        uiState = uiState,
        modifier = modifier
    ) { weatherData ->
        WeatherContent(
            weatherData = weatherData,
            showMainInfo = showMainInfo,
            showCurrentDetails = showCurrentDetails,
            showFullDetails = showFullDetails
        )
    }
}
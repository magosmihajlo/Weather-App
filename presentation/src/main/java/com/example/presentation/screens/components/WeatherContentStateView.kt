package com.example.presentation.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.state.WeatherUiState
import com.example.domain.model.WeatherDisplayData

@Composable
fun WeatherContentStateView(
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
        WeatherDetailsContent(
            weatherData = weatherData,
            showMainInfo = showMainInfo,
            showCurrentDetails = showCurrentDetails,
            showFullDetails = showFullDetails
        )
    }
}

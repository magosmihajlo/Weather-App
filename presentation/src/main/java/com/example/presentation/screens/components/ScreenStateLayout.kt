package com.example.presentation.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.presentation.state.WeatherUiState

@Composable
fun <T> ScreenStateLayout(
    uiState: WeatherUiState<T>,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is WeatherUiState.Loading -> CircularProgressIndicator()
            is WeatherUiState.Error -> Text(
                text = uiState.message,
                color = MaterialTheme.colorScheme.error
            )
            is WeatherUiState.Success -> content(uiState.data)
            is WeatherUiState.Empty -> Text("Search for a city to see the weather.")
        }
    }
}
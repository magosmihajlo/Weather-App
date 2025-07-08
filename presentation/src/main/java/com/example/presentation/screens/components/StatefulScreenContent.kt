package com.example.presentation.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.presentation.state.WeatherUiState

@Composable
fun <T> StatefulScreenContent(
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
            WeatherUiState.Empty -> Text("No data available.")
        }
    }
}

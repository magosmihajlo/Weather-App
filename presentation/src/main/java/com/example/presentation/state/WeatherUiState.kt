package com.example.presentation.state

sealed class WeatherUiState<out T> {
    object Loading : WeatherUiState<Nothing>()
    data class Success<out T>(val data: T) : WeatherUiState<T>()
    data class Error(val message: String) : WeatherUiState<Nothing>()
    object Empty : WeatherUiState<Nothing>()
}
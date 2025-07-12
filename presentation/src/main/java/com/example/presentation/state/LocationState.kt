package com.example.presentation.state

sealed class LocationState {
    data class Success(val cityName: String) : LocationState()
    data class Error(val message: String) : LocationState()
    data object Loading : LocationState()
}
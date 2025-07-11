package com.example.domain.model

data class DailyWeatherDisplayData(
    val dayName: String,
    val minTemperature: String,
    val maxTemperature: String,
    val iconUrl: String,
    val description: String
)

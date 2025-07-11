package com.example.domain.model

data class WeatherDisplayData(
    val locationName: String,
    val description: String,
    val iconUrl: String,
    val temperature: String,
    val minTemperature: String,
    val maxTemperature: String,
    val humidity: String,
    val windSpeed: String,
    val pressure: String,
    val visibility: String,
    val sunriseTime: String,
    val sunsetTime: String
)

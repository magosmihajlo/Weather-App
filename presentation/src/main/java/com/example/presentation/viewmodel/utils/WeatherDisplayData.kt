package com.example.presentation.viewmodel.utils

import java.time.format.DateTimeFormatter

val hourMinuteFormatter12 = DateTimeFormatter.ofPattern("hh:mm a")
val hourMinuteFormatter24 = DateTimeFormatter.ofPattern("HH:mm")

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
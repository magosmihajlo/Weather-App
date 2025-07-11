package com.example.domain.model

data class HourlyWeatherDisplayData(
    val time: String,
    val temperature: String,
    val iconUrl: String,
    val description: String
)

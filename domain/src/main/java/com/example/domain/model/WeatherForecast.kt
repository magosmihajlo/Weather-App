package com.example.domain.model

data class WeatherForecast(
    val hourly: List<HourlyWeather>,
    val daily: List<DailyWeather>
)

data class HourlyWeather(
    val timeEpoch: Long,
    val temperature: Double,
    val iconCode: String,
    val description: String
)

data class DailyWeather(
    val dateEpoch: Long,
    val minTemp: Double,
    val maxTemp: Double,
    val iconCode: String,
    val description: String
)


package com.example.data.remote.dto

data class ForecastResponseDto(
    val list: List<ForecastItemDto>
)

data class ForecastItemDto(
    val dt: Long,
    val main: ForecastMainDto,
    val weather: List<WeatherDescriptionDto>
)

data class ForecastMainDto(
    val temp: Double
)

data class WeatherDescriptionDto(
    val icon: String?,
    val description: String?
)

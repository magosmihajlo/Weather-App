package com.example.data.mapper

import com.example.data.remote.ForecastDto
import com.example.domain.model.HourlyWeather
import com.example.domain.model.WeatherForecast

fun ForecastDto.toDomain(): WeatherForecast {
    return WeatherForecast(
        hourly = list.take(24).map {
            HourlyWeather(
                timeEpoch = it.dt,
                temperature = it.main.temp,
                iconCode = it.weather.firstOrNull()?.icon ?: "",
                description = it.weather.firstOrNull()?.description ?: ""
            )
        },
        daily = emptyList()
    )
}


package com.example.data.mapper

import com.example.data.remote.dto.ForecastResponseDto
import com.example.domain.model.DailyWeather
import com.example.domain.model.HourlyWeather
import com.example.domain.model.WeatherForecast
import java.time.ZoneOffset

fun ForecastResponseDto.toDomain(): WeatherForecast {
    val hourlyWeather = list.take(24).map {
        HourlyWeather(
            timeEpoch = it.dt,
            temperature = it.main.temp,
            iconCode = it.weather.firstOrNull()?.icon ?: "",
            description = it.weather.firstOrNull()?.description ?: ""
        )
    }

    val dailyMap = list.groupBy { item ->
        java.time.Instant.ofEpochSecond(item.dt)
            .atZone(java.time.ZoneOffset.UTC)
            .toLocalDate()
    }

    val dailyWeather = dailyMap.entries.take(7).map { (date, items) ->
        val minTemp = items.minOf { it.main.temp }
        val maxTemp = items.maxOf { it.main.temp }
        val icon = items.firstOrNull()?.weather?.firstOrNull()?.icon ?: ""
        val description = items.firstOrNull()?.weather?.firstOrNull()?.description ?: ""

        DailyWeather(
            dateEpoch = date.atStartOfDay(ZoneOffset.UTC).toEpochSecond(),
            minTemp = minTemp,
            maxTemp = maxTemp,
            iconCode = icon,
            description = description
        )
    }

    return WeatherForecast(
        hourly = hourlyWeather,
        daily = dailyWeather
    )
}



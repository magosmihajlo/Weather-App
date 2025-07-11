package com.example.domain.repository.display

import com.example.domain.model.AppSettings
import com.example.domain.model.DailyWeather
import com.example.domain.model.DailyWeatherDisplayData
import com.example.domain.model.HourlyWeather
import com.example.domain.model.HourlyWeatherDisplayData

interface ForecastDisplayRepository {
    fun mapHourly(
        hourly: List<HourlyWeather>,
        timezoneOffsetSeconds: Int,
        settings: AppSettings
    ): List<HourlyWeatherDisplayData>

    fun mapDaily(
        daily: List<DailyWeather>,
        timezoneOffsetSeconds: Int,
        settings: AppSettings
    ): List<DailyWeatherDisplayData>
}

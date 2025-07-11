package com.example.domain.usecase.display

import com.example.domain.model.*
import com.example.domain.repository.display.ForecastDisplayRepository
import javax.inject.Inject

class MapHourlyForecastUseCase @Inject constructor(
    private val repository: ForecastDisplayRepository
) {
    operator fun invoke(
        hourly: List<HourlyWeather>,
        timezoneOffsetSeconds: Int,
        settings: AppSettings
    ): List<HourlyWeatherDisplayData> {
        return repository.mapHourly(hourly, timezoneOffsetSeconds, settings)
    }
}

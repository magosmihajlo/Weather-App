package com.example.domain.usecase.display

import com.example.domain.model.*
import com.example.domain.repository.display.ForecastDisplayRepository
import javax.inject.Inject

class MapDailyForecastUseCase @Inject constructor(
    private val repository: ForecastDisplayRepository
) {
    operator fun invoke(
        daily: List<DailyWeather>,
        timezoneOffsetSeconds: Int,
        settings: AppSettings
    ): List<DailyWeatherDisplayData> {
        return repository.mapDaily(daily, timezoneOffsetSeconds, settings)
    }
}

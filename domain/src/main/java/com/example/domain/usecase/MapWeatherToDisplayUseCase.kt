package com.example.domain.usecase

import com.example.domain.model.AppSettings
import com.example.domain.model.WeatherDisplayData
import com.example.domain.model.WeatherInfo
import com.example.domain.repository.WeatherDisplayRepository
import javax.inject.Inject

class MapWeatherToDisplayUseCase @Inject constructor(
    private val weatherDisplayRepository: WeatherDisplayRepository
) {
    operator fun invoke(weather: WeatherInfo, settings: AppSettings): WeatherDisplayData {
        return weatherDisplayRepository.mapToDisplay(weather, settings)
    }
}

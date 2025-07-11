package com.example.domain.usecase.display

import com.example.domain.model.AppSettings
import com.example.domain.model.WeatherDisplayData
import com.example.domain.model.WeatherInfo
import com.example.domain.repository.display.WeatherDisplayRepository
import javax.inject.Inject

class MapWeatherUseCase @Inject constructor(
    private val weatherDisplayRepository: WeatherDisplayRepository
) {
    operator fun invoke(weather: WeatherInfo, settings: AppSettings): WeatherDisplayData {
        return weatherDisplayRepository.mapToDisplay(weather, settings)
    }
}
package com.example.domain.usecase

import com.example.domain.model.WeatherInfo
import com.example.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): WeatherInfo {
        return repository.getCurrentWeather(city)
    }
}
package com.example.domain.usecase.api

import com.example.domain.model.WeatherForecast
import com.example.domain.repository.api.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): WeatherForecast {
        return repository.getForecast(lat, lon)
    }
}
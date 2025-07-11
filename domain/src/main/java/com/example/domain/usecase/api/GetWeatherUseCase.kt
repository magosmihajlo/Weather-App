package com.example.domain.usecase.api

import com.example.domain.model.DailyWeather
import com.example.domain.model.WeatherInfo
import com.example.domain.repository.api.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String, todayForecast: DailyWeather? = null): WeatherInfo {
        return repository.getCurrentWeather(city, todayForecast)
    }
}
package com.example.domain.repository.api

import com.example.domain.model.DailyWeather
import com.example.domain.model.WeatherForecast
import com.example.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getCurrentWeather(city: String, todayForecast: DailyWeather? = null): WeatherInfo

    suspend fun getForecast(lat: Double, lon: Double): WeatherForecast
}
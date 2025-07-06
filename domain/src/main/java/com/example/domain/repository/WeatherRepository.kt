package com.example.domain.repository

import com.example.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getCurrentWeather(city: String): WeatherInfo
}
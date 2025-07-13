package com.example.weatherapp

import com.example.domain.DiConfigurator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfigurator @Inject constructor() : DiConfigurator {
    override fun getWeatherApiKey(): String {
        return BuildConfig.WEATHER_API_KEY
    }
}
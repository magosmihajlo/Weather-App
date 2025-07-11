package com.example.domain.repository.display

import com.example.domain.model.AppSettings
import com.example.domain.model.WeatherDisplayData
import com.example.domain.model.WeatherInfo

interface WeatherDisplayRepository {
    fun mapToDisplay(weather: WeatherInfo, settings: AppSettings): WeatherDisplayData
}

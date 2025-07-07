package com.example.presentation.utils

import com.example.domain.model.AppSettings
import com.example.domain.model.RecentCity
import com.example.domain.usecase.ConvertTemperatureUseCase
import com.example.presentation.uimodel.WeatherDisplayData
import jakarta.inject.Inject

class RecentCityUiMapper @Inject constructor(
    private val convertTemperature: ConvertTemperatureUseCase
) {
    fun map(cities: List<RecentCity>, settings: AppSettings): List<WeatherDisplayData> {
        return cities.map {
            WeatherDisplayData(
                locationName = it.cityName,
                description = it.description,
                iconUrl = "https://openweathermap.org/img/wn/${it.iconCode}@2x.png",
                temperature = "%.1f%s".format(convertTemperature(it.temperature, settings.temperatureUnit), settings.temperatureUnit.label),
                minTemperature = "",
                maxTemperature = "",
                feelsLikeTemperature = "",
                humidity = "",
                windSpeed = "",
                pressure = "",
                visibility = "",
                sunriseTime = "",
                sunsetTime = ""
            )
        }
    }
}

package com.example.data.repository.general

import com.example.domain.repository.api.WeatherRepository
import com.example.domain.repository.database.RecentCityRepository
import com.example.domain.repository.database.UpdateCitiesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateCitiesRepositoryImpl @Inject constructor(
    private val recentCityRepository: RecentCityRepository,
    private val weatherRepository: WeatherRepository
) : UpdateCitiesRepository {

    override suspend fun updateCitiesWeather() {
        val cityNames = recentCityRepository.getRecentCities().first().map { it.cityName }

        cityNames.forEach { cityName ->
            try {
                val weatherInfo = weatherRepository.getCurrentWeather(cityName)
                recentCityRepository.saveRecentCity(
                    cityName = weatherInfo.cityName,
                    temperature = weatherInfo.temperatureCelsius,
                    iconCode = weatherInfo.iconCode,
                    description = weatherInfo.description
                )
            } catch (e: Exception) {
            }
        }
    }
}
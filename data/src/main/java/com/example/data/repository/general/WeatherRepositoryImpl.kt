package com.example.data.repository.general

import com.example.data.mapper.toDomain
import com.example.data.remote.api.WeatherApiService
import com.example.domain.model.DailyWeather
import com.example.domain.model.WeatherForecast
import com.example.domain.model.WeatherInfo
import com.example.domain.repository.api.WeatherRepository
import javax.inject.Inject
import javax.inject.Named

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService,
    @Named("api_key") private val apiKey: String
) : WeatherRepository {

    override suspend fun getCurrentWeather(city: String, todayForecast: DailyWeather?): WeatherInfo {
        return api.getCurrentWeather(city = city, apiKey = apiKey).toDomain()
    }

    override suspend fun getForecast(lat: Double, lon: Double): WeatherForecast {
        val dto = api.getForecast(
            lat = lat,
            lon = lon,
            apiKey = apiKey
        )
        return dto.toDomain()
    }

}

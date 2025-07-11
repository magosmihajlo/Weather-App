package com.example.data.repository.display

import com.example.domain.model.AppSettings
import com.example.domain.model.RecentCity
import com.example.domain.model.WeatherDisplayData
import com.example.domain.repository.display.RecentCityDisplayRepository
import com.example.domain.repository.settings.UnitConversionRepository
import javax.inject.Inject

class RecentCityDisplayRepositoryImpl @Inject constructor(
    private val unitConverter: UnitConversionRepository
) : RecentCityDisplayRepository {

    override fun mapToDisplay(cities: List<RecentCity>, settings: AppSettings): List<WeatherDisplayData> {
        return cities.map {
            WeatherDisplayData(
                locationName = it.cityName,
                description = it.description,
                iconUrl = "https://openweathermap.org/img/wn/${it.iconCode}@2x.png",
                temperature = "%.1f%s".format(
                    unitConverter.convertTemperature(it.temperature, settings.temperatureUnit),
                    settings.temperatureUnit.label
                ),
                minTemperature = "",
                maxTemperature = "",
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

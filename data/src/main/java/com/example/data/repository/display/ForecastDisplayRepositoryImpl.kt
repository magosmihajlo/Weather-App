package com.example.data.repository.display

import com.example.data.mapper.TimeFormatter
import com.example.domain.model.*
import com.example.domain.repository.display.ForecastDisplayRepository
import com.example.domain.repository.settings.UnitConversionRepository
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ForecastDisplayRepositoryImpl @Inject constructor(
    private val converter: UnitConversionRepository
) : ForecastDisplayRepository {

    override fun mapHourly(
        hourly: List<HourlyWeather>,
        timezoneOffsetSeconds: Int,
        settings: AppSettings
    ): List<HourlyWeatherDisplayData> {
        return hourly.map {
            HourlyWeatherDisplayData(
                time = TimeFormatter.formatTime(it.timeEpoch, timezoneOffsetSeconds, settings.timeFormat),
                temperature = String.format("%.1f%s", converter.convertTemperature(it.temperature, settings.temperatureUnit), settings.temperatureUnit.label),
                iconUrl = "https://openweathermap.org/img/wn/${it.iconCode}@2x.png",
                description = it.description
            )
        }
    }

    override fun mapDaily(
        daily: List<DailyWeather>,
        timezoneOffsetSeconds: Int,
        settings: AppSettings
    ): List<DailyWeatherDisplayData> {
        val formatter = DateTimeFormatter.ofPattern("EEE")
        val zone = ZoneOffset.ofTotalSeconds(timezoneOffsetSeconds)

        return daily.map {
            DailyWeatherDisplayData(
                dayName = Instant.ofEpochSecond(it.dateEpoch).atZone(zone).format(formatter),
                minTemperature = "Min: %.1f%s".format(converter.convertTemperature(it.minTemp, settings.temperatureUnit), settings.temperatureUnit.label),
                maxTemperature = "Max: %.1f%s".format(converter.convertTemperature(it.maxTemp, settings.temperatureUnit), settings.temperatureUnit.label),
                iconUrl = "https://openweathermap.org/img/wn/${it.iconCode}@2x.png",
                description = it.description
            )
        }
    }
}

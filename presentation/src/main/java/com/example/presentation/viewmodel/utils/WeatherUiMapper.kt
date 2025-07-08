package com.example.presentation.viewmodel.utils

import com.example.domain.model.AppSettings
import com.example.domain.model.TimeFormat
import com.example.domain.model.WeatherInfo
import com.example.domain.usecase.conversion.ConvertPressureUseCase
import com.example.domain.usecase.conversion.ConvertTemperatureUseCase
import com.example.domain.usecase.conversion.ConvertWindSpeedUseCase
import jakarta.inject.Inject
import java.time.Instant
import java.time.ZoneId

class WeatherUiMapper @Inject constructor(
    private val convertTemperature: ConvertTemperatureUseCase,
    private val convertWindSpeed: ConvertWindSpeedUseCase,
    private val convertPressure: ConvertPressureUseCase
) {
    fun map(info: WeatherInfo, settings: AppSettings): WeatherDisplayData {
        val temp = convertTemperature(info.temperatureCelsius, settings.temperatureUnit)
        val minTemp = convertTemperature(info.minTemperatureCelsius, settings.temperatureUnit)
        val maxTemp = convertTemperature(info.maxTemperatureCelsius, settings.temperatureUnit)
        val feels = convertTemperature(info.feelsLikeCelsius, settings.temperatureUnit)
        val wind = convertWindSpeed(info.windSpeedKmh, settings.windSpeedUnit)
        val pressure = convertPressure(info.pressureHPa.toDouble(), settings.pressureUnit)

        val formatter = if (settings.timeFormat == TimeFormat.TWELVE_HOUR) hourMinuteFormatter12 else hourMinuteFormatter24

        return WeatherDisplayData(
            locationName = info.cityName,
            description = info.description,
            iconUrl = "https://openweathermap.org/img/wn/${info.iconCode}@2x.png",
            temperature = "%.1f%s".format(temp, settings.temperatureUnit.label),
            minTemperature = "Min: %.1f%s".format(minTemp, settings.temperatureUnit.label),
            maxTemperature = "Max: %.1f%s".format(maxTemp, settings.temperatureUnit.label),
            feelsLikeTemperature = "Feels Like: %.1f%s".format(feels, settings.temperatureUnit.label),
            humidity = "${info.humidityPercent}%",
            windSpeed = "%.1f %s".format(wind, settings.windSpeedUnit.label),
            pressure = "%.0f %s".format(pressure, settings.pressureUnit.label),
            visibility = "%.0f km".format(info.visibilityMeters / 1000.0),
            sunriseTime = Instant.ofEpochSecond(info.sunriseTime).atZone(ZoneId.systemDefault()).format(formatter),
            sunsetTime = Instant.ofEpochSecond(info.sunsetTime).atZone(ZoneId.systemDefault()).format(formatter)
        )
    }
}

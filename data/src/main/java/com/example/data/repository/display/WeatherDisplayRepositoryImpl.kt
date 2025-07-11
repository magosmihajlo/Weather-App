package com.example.data.repository.display

import com.example.data.mapper.TimeFormatter
import com.example.domain.model.AppSettings
import com.example.domain.model.TemperatureUnit
import com.example.domain.model.WeatherDisplayData
import com.example.domain.model.WeatherInfo
import com.example.domain.repository.settings.UnitConversionRepository
import com.example.domain.repository.display.WeatherDisplayRepository
import javax.inject.Inject

class WeatherDisplayRepositoryImpl @Inject constructor(
    private val unitConverter: UnitConversionRepository
) : WeatherDisplayRepository {

    override fun mapToDisplay(weather: WeatherInfo, settings: AppSettings): WeatherDisplayData {
        return WeatherDisplayData(
            locationName = weather.cityName,
            iconUrl = "https://openweathermap.org/img/wn/${weather.iconCode}@2x.png",
            temperature = formatTemp(
                unitConverter.convertTemperature(weather.temperatureCelsius, settings.temperatureUnit),
                settings.temperatureUnit
            ),
            description = weather.description.replaceFirstChar { it.uppercase() },
            minTemperature = "Min: " + formatTemp(
                unitConverter.convertTemperature(weather.minTemperatureCelsius, settings.temperatureUnit),
                settings.temperatureUnit
            ),
            maxTemperature = "Max: " + formatTemp(
                unitConverter.convertTemperature(weather.maxTemperatureCelsius, settings.temperatureUnit),
                settings.temperatureUnit
            ),
            humidity = "Humidity: ${weather.humidityPercent}%",
            windSpeed = "Wind: " + formatSpeed(
                unitConverter.convertWindSpeed(weather.windSpeedKmh, settings.windSpeedUnit)
            ),
            pressure = "Pressure: " + formatPressure(
                unitConverter.convertPressure(weather.pressureHPa.toDouble(), settings.pressureUnit)
            ),
            visibility = "Visibility: ${weather.visibilityMeters / 1000.0} km",
            sunriseTime = "Sunrise: ${TimeFormatter.formatTime(weather.sunriseTime, weather.timezoneOffsetSeconds, settings.timeFormat)}",
            sunsetTime = "Sunset: ${TimeFormatter.formatTime(weather.sunsetTime, weather.timezoneOffsetSeconds, settings.timeFormat)}"

        )
    }

    private fun formatTemp(value: Double, unit: TemperatureUnit): String =
        String.format("%.1f%s", value, unit.label)
    private fun formatSpeed(value: Double): String = String.format("%.1f", value)
    private fun formatPressure(value: Double): String = String.format("%.1f", value)
}

package com.example.data.repository.display

import com.example.data.mapper.TimeFormatter
import com.example.domain.model.*
import com.example.domain.repository.display.WeatherDisplayRepository
import com.example.domain.repository.settings.UnitConversionRepository
import javax.inject.Inject

class WeatherDisplayRepositoryImpl @Inject constructor(
    private val unitConverter: UnitConversionRepository
) : WeatherDisplayRepository {

    override fun mapToDisplay(weather: WeatherInfo, settings: AppSettings): WeatherDisplayData {
        val temperature = unitConverter.convertTemperature(weather.temperatureCelsius, settings.temperatureUnit)
        val minTemp = unitConverter.convertTemperature(weather.minTemperatureCelsius, settings.temperatureUnit)
        val maxTemp = unitConverter.convertTemperature(weather.maxTemperatureCelsius, settings.temperatureUnit)
        val wind = unitConverter.convertWindSpeed(weather.windSpeedKmh, settings.windSpeedUnit)
        val pressure = unitConverter.convertPressure(weather.pressureHPa.toDouble(), settings.pressureUnit)
        val visibilityKm = weather.visibilityMeters / 1000.0

        return WeatherDisplayData(
            locationName = weather.cityName,
            iconUrl = "https://openweathermap.org/img/wn/${weather.iconCode}@2x.png",
            temperature = formatTemp(temperature, settings.temperatureUnit),
            description = weather.description.replaceFirstChar { it.uppercase() },
            minTemperature = "Min: ${formatTemp(minTemp, settings.temperatureUnit)}",
            maxTemperature = "Max: ${formatTemp(maxTemp, settings.temperatureUnit)}",
            humidity = "Humidity: ${weather.humidityPercent}%",
            windSpeed = "Wind: ${formatSpeed(wind, settings.windSpeedUnit)}",
            pressure = "Pressure: ${formatPressure(pressure, settings.pressureUnit)}",
            visibility = "Visibility: %.1f km".format(visibilityKm),
            sunriseTime = "Sunrise: ${TimeFormatter.formatTime(weather.sunriseTime, weather.timezoneOffsetSeconds, settings.timeFormat)}",
            sunsetTime = "Sunset: ${TimeFormatter.formatTime(weather.sunsetTime, weather.timezoneOffsetSeconds, settings.timeFormat)}"
        )
    }

    private fun formatTemp(value: Double, unit: TemperatureUnit): String =
        String.format("%.1f%s", value, unit.label)

    private fun formatSpeed(value: Double, unit: WindSpeedUnit): String =
        String.format("%.1f %s", value, unit.label)

    private fun formatPressure(value: Double, unit: PressureUnit): String =
        String.format("%.1f %s", value, unit.label)
}

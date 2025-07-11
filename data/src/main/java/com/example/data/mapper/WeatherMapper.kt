package com.example.data.mapper


import com.example.data.remote.dto.WeatherResponseDto
import com.example.domain.model.WeatherInfo
import com.example.data.local.entity.RecentCityEntity
import com.example.domain.model.RecentCity

fun WeatherResponseDto.toDomain(): WeatherInfo {
    val weather = weather.firstOrNull()


    return WeatherInfo(
        temperatureCelsius = main.temp.round1Decimal(),
        feelsLikeCelsius = main.feelsLike.round1Decimal(),
        minTemperatureCelsius = if (main.tempMin == 0.0) main.temp else main.tempMin,
        maxTemperatureCelsius = if (main.tempMax == 0.0) main.temp else main.tempMax,
        humidityPercent = main.humidity,
        pressureHPa = main.pressure,
        seaLevelPressureHPa = main.seaLevel,
        groundLevelPressureHPa = main.grndLevel,
        windSpeedKmh = (wind.speed * 3.6).round1Decimal(),
        windDirectionDegrees = wind.deg,
        description = weather?.description ?: "No description",
        iconCode = weather?.icon ?: "",
        cloudinessPercent = clouds.all,
        visibilityMeters = visibility,
        countryCode = sys.country,
        sunriseTime = sys.sunrise,
        sunsetTime = sys.sunset,
        cityId = id,
        cityName = name,
        timestampMillis = dt * 1000L,
        latitude = coord.lat,
        longitude = coord.lon,
        rainVolumeLast1hMm = rain?.last1h?.round1Decimal(),
        rainVolumeLast3hMm = rain?.last3h?.round1Decimal(),
        snowVolumeLast1hMm = snow?.last1h?.round1Decimal(),
        snowVolumeLast3hMm = snow?.last3h?.round1Decimal(),
        timezoneOffsetSeconds = timezone
    )
}

private fun Double.round1Decimal(): Double {
    return ((this * 10).toInt()) / 10.0
}


fun RecentCityEntity.toDomain(): RecentCity {
    return RecentCity(
        cityName = this.cityName,
        temperature = this.temperature,
        iconCode = this.iconCode,
        description = this.description,
        lastAccessed = this.lastAccessed
    )
}
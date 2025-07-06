package com.example.data.remote

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    val coord: CoordDto,
    val weather: List<WeatherDto>,
    val base: String,
    val main: MainDto,
    val visibility: Int,
    val wind: WindDto,
    val clouds: CloudsDto,
    val dt: Long,
    val sys: SysDto,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int,
    val rain: RainDto? = null,
    val snow: SnowDto? = null
)

data class MainDto(
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val seaLevel: Int?,
    val grndLevel: Int?
)

data class WeatherDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class WindDto(
    val speed: Double,
    val deg: Int
)

data class CoordDto(
    val lon: Double,
    val lat: Double
)

data class CloudsDto(
    val all: Int
)

data class SysDto(
    val type: Int?,
    val id: Int?,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

data class RainDto(
    @SerializedName("1h")
    val last1h: Double?,
    @SerializedName("3h")
    val last3h: Double?
)

data class SnowDto(
    @SerializedName("1h")
    val last1h: Double?,
    @SerializedName("3h")
    val last3h: Double?
)
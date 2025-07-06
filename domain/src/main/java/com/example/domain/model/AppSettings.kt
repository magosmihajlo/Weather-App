package com.example.domain.model

data class AppSettings(
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val pressureUnit: PressureUnit = PressureUnit.HPA,
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.KM_PER_HOUR,
    val timeFormat: TimeFormat = TimeFormat.TWENTY_FOUR_HOUR,
    val defaultCity: String = "Novi Sad",
    val locationEnabled: Boolean = false,
    val notificationsEnabled: Boolean = false
)

enum class TemperatureUnit(val label: String) {
    CELSIUS("°C"),
    FAHRENHEIT("°F")
}

enum class PressureUnit(val label: String) {
    HPA("hPa"),
    INCHES("Inches"),
    KPA("kPa"),
    MM("mm")
}

enum class WindSpeedUnit(val label: String) {
    KM_PER_HOUR("km/h"),
    METERS_PER_SECOND("m/s"),
    KNOTS("Knots")
}

enum class TimeFormat(val label: String) {
    TWELVE_HOUR("12-Hour Time"),
    TWENTY_FOUR_HOUR("24-Hour Format")
}
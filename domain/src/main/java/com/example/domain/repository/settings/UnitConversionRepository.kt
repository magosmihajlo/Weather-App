package com.example.domain.repository.settings

import com.example.domain.model.PressureUnit
import com.example.domain.model.TemperatureUnit
import com.example.domain.model.WindSpeedUnit

interface UnitConversionRepository {
    fun convertTemperature(celsius: Double, targetUnit: TemperatureUnit): Double
    fun convertWindSpeed(kmh: Double, targetUnit: WindSpeedUnit): Double
    fun convertPressure(hPa: Double, targetUnit: PressureUnit): Double
}

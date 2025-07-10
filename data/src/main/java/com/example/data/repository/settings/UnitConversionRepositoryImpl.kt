package com.example.data.repository.settings

import com.example.domain.model.PressureUnit
import com.example.domain.model.TemperatureUnit
import com.example.domain.model.WindSpeedUnit
import com.example.domain.repository.UnitConversionRepository
import javax.inject.Inject

class UnitConversionRepositoryImpl @Inject constructor() : UnitConversionRepository {

    private val kmhToMsFactor = 1000.0 / 3600.0
    private val khmToKnotsFactor = 1.0 / 1.852
    private val hpaToInches = 0.02953
    private val hpaToKpa = 0.1
    private val hpaToMm = 0.750062

    override fun convertTemperature(celsius: Double, targetUnit: TemperatureUnit): Double {
        return when (targetUnit) {
            TemperatureUnit.CELSIUS -> celsius
            TemperatureUnit.FAHRENHEIT -> (celsius * 9 / 5) + 32
        }
    }

    override fun convertWindSpeed(kmh: Double, targetUnit: WindSpeedUnit): Double {
        return when (targetUnit) {
            WindSpeedUnit.KM_PER_HOUR -> kmh
            WindSpeedUnit.METERS_PER_SECOND -> kmh * kmhToMsFactor
            WindSpeedUnit.KNOTS -> kmh * khmToKnotsFactor
        }
    }

    override fun convertPressure(hPa: Double, targetUnit: PressureUnit): Double {
        return when (targetUnit) {
            PressureUnit.HPA -> hPa
            PressureUnit.INCHES -> hPa * hpaToInches
            PressureUnit.KPA -> hPa * hpaToKpa
            PressureUnit.MM -> hPa * hpaToMm
        }
    }
}

package com.example.domain.usecase

import com.example.domain.model.WindSpeedUnit
import com.example.domain.repository.UnitConversionRepository
import javax.inject.Inject

class ConvertWindSpeedUseCase @Inject constructor(
    private val repository: UnitConversionRepository
) {
    operator fun invoke(kmh: Double, targetUnit: WindSpeedUnit): Double {
        return repository.convertWindSpeed(kmh, targetUnit)
    }
}

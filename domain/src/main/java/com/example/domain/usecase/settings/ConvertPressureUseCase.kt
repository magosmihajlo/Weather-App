package com.example.domain.usecase.settings

import com.example.domain.model.PressureUnit
import com.example.domain.repository.settings.UnitConversionRepository
import javax.inject.Inject

class ConvertPressureUseCase @Inject constructor(
    private val repository: UnitConversionRepository
) {
    operator fun invoke(hPa: Double, targetUnit: PressureUnit): Double {
        return repository.convertPressure(hPa, targetUnit)
    }
}
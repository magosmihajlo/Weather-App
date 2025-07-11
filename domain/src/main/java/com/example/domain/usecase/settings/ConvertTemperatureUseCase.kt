package com.example.domain.usecase.settings

import com.example.domain.model.TemperatureUnit
import com.example.domain.repository.settings.UnitConversionRepository
import javax.inject.Inject

class ConvertTemperatureUseCase @Inject constructor(
    private val repository: UnitConversionRepository
) {
    operator fun invoke(celsius: Double, targetUnit: TemperatureUnit): Double {
        return repository.convertTemperature(celsius, targetUnit)
    }
}

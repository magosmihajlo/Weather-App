package com.example.domain.usecase.settings

import com.example.domain.model.*
import com.example.domain.repository.settings.AppSettingsRepository
import javax.inject.Inject

class UpdateAppSettingsUseCase @Inject constructor(
    private val repository: AppSettingsRepository
) {
    suspend fun updateTemperatureUnit(unit: TemperatureUnit) {
        repository.updateTemperatureUnit(unit)
    }

    suspend fun updateWindSpeedUnit(unit: WindSpeedUnit) {
        repository.updateWindSpeedUnit(unit)
    }

    suspend fun updatePressureUnit(unit: PressureUnit) {
        repository.updatePressureUnit(unit)
    }

    suspend fun updateNotificationsEnabled(enabled: Boolean) {
        repository.updateNotificationsEnabled(enabled)
    }

    suspend fun updateTimeFormat(format: TimeFormat) {
        repository.updateTimeFormat(format)
    }

    suspend fun updateLocationEnabled(enabled: Boolean) {
        repository.updateLocationEnabled(enabled)
    }

    suspend fun updateThemeMode(mode: ThemeMode) {
        repository.updateThemeMode(mode)
    }
}

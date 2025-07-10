package com.example.domain.repository

import com.example.domain.model.AppSettings
import com.example.domain.model.PressureUnit
import com.example.domain.model.TemperatureUnit
import com.example.domain.model.ThemeMode
import com.example.domain.model.TimeFormat
import com.example.domain.model.WindSpeedUnit
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    val appSettingsFlow: Flow<AppSettings>

    suspend fun updateTemperatureUnit(unit: TemperatureUnit)
    suspend fun updatePressureUnit(unit: PressureUnit)
    suspend fun updateWindSpeedUnit(unit: WindSpeedUnit)
    suspend fun updateTimeFormat(format: TimeFormat)
    suspend fun updateLocationEnabled(enabled: Boolean)
    suspend fun updateNotificationsEnabled(enabled: Boolean)
    suspend fun updateThemeMode(mode: ThemeMode)

}
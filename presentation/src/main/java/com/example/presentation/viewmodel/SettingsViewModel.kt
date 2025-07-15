package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.*
import com.example.domain.usecase.settings.GetAppSettingsUseCase
import com.example.domain.usecase.settings.UpdateAppSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase
) : ViewModel() {

    val appSettings: StateFlow<AppSettings> = getAppSettingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    fun updateTemperatureUnit(unit: TemperatureUnit) {
        viewModelScope.launch {
            updateAppSettingsUseCase.updateTemperatureUnit(unit)
        }
    }

    fun updateWindSpeedUnit(unit: WindSpeedUnit) {
        viewModelScope.launch {
            updateAppSettingsUseCase.updateWindSpeedUnit(unit)
        }
    }

    fun updatePressureUnit(unit: PressureUnit) {
        viewModelScope.launch {
            updateAppSettingsUseCase.updatePressureUnit(unit)
        }
    }

    fun updateNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            updateAppSettingsUseCase.updateNotificationsEnabled(enabled)
        }
    }

    fun updateTimeFormat(format: TimeFormat) {
        viewModelScope.launch {
            updateAppSettingsUseCase.updateTimeFormat(format)
        }
    }

    fun updateLocationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            updateAppSettingsUseCase.updateLocationEnabled(enabled)
        }
    }

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            updateAppSettingsUseCase.updateThemeMode(mode)
        }
    }
}

package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AppSettings
import com.example.domain.model.PressureUnit
import com.example.domain.model.TimeFormat
import com.example.domain.model.WindSpeedUnit
import com.example.domain.repository.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) : ViewModel() {

    val appSettings: StateFlow<AppSettings> = appSettingsRepository.appSettingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    fun updatePressureUnit(unit: PressureUnit) {
        viewModelScope.launch {
            appSettingsRepository.updatePressureUnit(unit)
        }
    }

    fun updateWindSpeedUnit(unit: WindSpeedUnit) {
        viewModelScope.launch {
            appSettingsRepository.updateWindSpeedUnit(unit)
        }
    }

    fun updateTimeFormat(format: TimeFormat) {
        viewModelScope.launch {
            appSettingsRepository.updateTimeFormat(format)
        }
    }

    fun updateLocationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.updateLocationEnabled(enabled)
        }
    }

    fun updateNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.updateNotificationsEnabled(enabled)
        }
    }
}
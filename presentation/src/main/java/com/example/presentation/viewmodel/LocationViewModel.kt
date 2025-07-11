package com.example.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.location.GetCityNameUseCase
import com.example.domain.usecase.location.GetCurrentLocationUseCase
import com.example.domain.usecase.location.HasLocationPermissionUseCase
import com.example.domain.usecase.settings.GetAppSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LocationResult {
    data class Success(val cityName: String) : LocationResult()
    data class Error(val message: String) : LocationResult()
    data object Loading : LocationResult()
}

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val hasLocationPermissionUseCase: HasLocationPermissionUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getCityNameUseCase: GetCityNameUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase
) : ViewModel() {

    private val _permissionRequestChannel = Channel<Unit>()
    val permissionRequestFlow = _permissionRequestChannel.receiveAsFlow()

    private val _locationResultChannel = Channel<LocationResult>()
    val locationResultFlow = _locationResultChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            getAppSettingsUseCase()
                .map { it.locationEnabled }
                .distinctUntilChanged()
                .collect { isEnabled ->
                    if (isEnabled) {
                        onLocationRequested()
                    }
                }
        }
    }

    fun onLocationRequested() {
        viewModelScope.launch {
            if (hasLocationPermissionUseCase()) {
                fetchLocation()
            } else {
                _permissionRequestChannel.send(Unit)
            }
        }
    }

    fun onPermissionResult(granted: Boolean) {
        viewModelScope.launch {
            if (granted) {
                fetchLocation()
            } else {
                _locationResultChannel.send(LocationResult.Error("Location permission denied."))
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        viewModelScope.launch {
            _locationResultChannel.send(LocationResult.Loading)
            try {
                val location = getCurrentLocationUseCase()
                val city = location?.let {
                    getCityNameUseCase(it.latitude, it.longitude)
                }

                if (city != null) {
                    _locationResultChannel.send(LocationResult.Success(city))
                } else {
                    _locationResultChannel.send(LocationResult.Error("Could not determine city name."))
                }
            } catch (e: Exception) {
                _locationResultChannel.send(LocationResult.Error("Failed to get location: ${e.message}"))
            }
        }
    }
}
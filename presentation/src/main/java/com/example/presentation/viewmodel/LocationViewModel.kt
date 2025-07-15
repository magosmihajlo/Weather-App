package com.example.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.location.GetCityNameUseCase
import com.example.domain.usecase.location.GetCurrentLocationUseCase
import com.example.domain.usecase.location.HasLocationPermissionUseCase
import com.example.domain.usecase.settings.GetAppSettingsUseCase
import com.example.domain.usecase.settings.UpdateAppSettingsUseCase
import com.example.presentation.state.LocationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val hasLocationPermissionUseCase: HasLocationPermissionUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getCityNameUseCase: GetCityNameUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase
) : ViewModel() {

    private val _permissionRequestChannel = Channel<Unit>()
    val permissionRequestFlow = _permissionRequestChannel.receiveAsFlow()

    private val _locationResultChannel = Channel<LocationState>()
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
                updateAppSettingsUseCase.updateLocationEnabled(false)
                _locationResultChannel.send(LocationState.Error("Location permission denied."))
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        viewModelScope.launch {
            _locationResultChannel.send(LocationState.Loading)
            try {
                val location = getCurrentLocationUseCase()
                val city = location?.let {
                    getCityNameUseCase(it.latitude, it.longitude)
                }

                if (city != null) {
                    _locationResultChannel.send(LocationState.Success(city))
                } else {
                    _locationResultChannel.send(LocationState.Error("Could not determine city name."))
                }
            } catch (e: Exception) {
                _locationResultChannel.send(LocationState.Error("Failed to get location: ${e.message}"))
            }
        }
    }
}
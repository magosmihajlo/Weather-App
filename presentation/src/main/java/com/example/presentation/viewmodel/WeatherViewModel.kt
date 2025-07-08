package com.example.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.WeatherInfo
import com.example.domain.usecase.GetAppSettingsUseCase
import com.example.presentation.uimodel.WeatherDisplayData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import jakarta.inject.Inject
import com.example.domain.usecase.SaveRecentCityUseCase
import com.example.domain.usecase.GetRecentCitiesUseCase
import com.example.domain.usecase.GetWeatherUseCase
import com.example.presentation.state.WeatherUiState
import com.example.presentation.utils.CityNameResolver
import com.example.presentation.utils.LocationProvider
import com.example.presentation.utils.RecentCityUiMapper
import com.example.presentation.utils.WeatherUiMapper
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val saveRecentCityUseCase: SaveRecentCityUseCase,
    private val getRecentCitiesUseCase: GetRecentCitiesUseCase,
    private val weatherUiMapper: WeatherUiMapper,
    private val recentCityUiMapper: RecentCityUiMapper,
    private val locationProvider: LocationProvider,
    private val cityNameResolver: CityNameResolver
) : ViewModel() {

    private val _rawWeatherInfo = MutableStateFlow<WeatherInfo?>(null)
    private val _currentCity = MutableStateFlow("Belgrade") // Default city

    private val _uiState = MutableStateFlow<WeatherUiState<WeatherDisplayData>>(WeatherUiState.Empty)
    val uiState = _uiState.asStateFlow()


    private val _recentCitiesUiState = MutableStateFlow<WeatherUiState<List<WeatherDisplayData>>>(WeatherUiState.Empty)
    val recentCitiesUiState = _recentCitiesUiState.asStateFlow()


    private val _requestLocationPermission = MutableStateFlow(false)
    val requestLocationPermission = _requestLocationPermission.asStateFlow()

    init {
        observeAppSettings()
        observeRecentCities()

        viewModelScope.launch {
            combine(
                _rawWeatherInfo.filterNotNull(),
                getAppSettingsUseCase()
            ) { raw, settings ->
                weatherUiMapper.map(raw, settings)
            }.collect { displayData ->
                _uiState.value = WeatherUiState.Success(displayData)
            }
        }
    }

    private fun observeAppSettings() {
        viewModelScope.launch {
            getAppSettingsUseCase()
                .map { it.locationEnabled }
                .distinctUntilChanged()
                .collect { locationEnabled ->
                    handleLocationEnabledChange(locationEnabled)
                }
        }
    }

    private fun observeRecentCities() {
        viewModelScope.launch {
            _recentCitiesUiState.value = WeatherUiState.Loading
            getRecentCitiesUseCase().collect { recentCities ->
                if (recentCities.isEmpty()) {
                    _recentCitiesUiState.value = WeatherUiState.Empty
                } else {
                    val settings = getAppSettingsUseCase().first()
                    val displayData = recentCityUiMapper.map(recentCities, settings)
                    _recentCitiesUiState.value = WeatherUiState.Success(displayData)
                }
            }
        }
    }


    private fun handleLocationEnabledChange(locationEnabled: Boolean) {
        if (locationEnabled) {
            if (locationProvider.hasLocationPermission()) {
                fetchCurrentLocationWeather()
            } else {
                _requestLocationPermission.value = true
                fetchWeatherByCityName(_currentCity.value)
            }
        } else {
            fetchWeatherByCityName(_currentCity.value)
        }
    }

    fun searchWeather(cityName: String) {
        if (cityName.isNotBlank()) {
            _currentCity.value = cityName
            fetchWeatherByCityName(cityName)
        } else {
            _uiState.value = WeatherUiState.Error("City name cannot be empty.")
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchCurrentLocationWeather() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            _requestLocationPermission.value = false

            val location = locationProvider.getCurrentLocation()
            if (location != null) {
                val city = cityNameResolver.getCityName(location.latitude, location.longitude)
                if (city != null) {
                    _currentCity.value = city
                    fetchWeatherByCityName(city)
                } else {
                    _uiState.value = WeatherUiState.Error("Could not determine city from location.")
                    fetchWeatherByCityName(_currentCity.value)
                }
            } else {
                _uiState.value = WeatherUiState.Error("Could not get location.")
                fetchWeatherByCityName(_currentCity.value)
            }
        }
    }

    private fun fetchWeatherByCityName(cityName: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val weatherInfo = getWeatherUseCase(cityName)
                _rawWeatherInfo.value = weatherInfo
                _currentCity.value = cityName

                saveRecentCityUseCase(
                    cityName = weatherInfo.cityName,
                    temperature = weatherInfo.temperatureCelsius,
                    iconCode = weatherInfo.iconCode,
                    description = weatherInfo.description
                )
            } catch (e: Exception) {
                val msg = e.message ?: "Unknown error"
                _uiState.value = WeatherUiState.Error("Failed to fetch weather for $cityName: $msg")
                _rawWeatherInfo.value = null
            }
        }
    }

    fun onLocationPermissionResult(granted: Boolean) {
        if (granted) {
            fetchCurrentLocationWeather()
        } else {
            searchWeather("Belgrade")
        }
    }
}
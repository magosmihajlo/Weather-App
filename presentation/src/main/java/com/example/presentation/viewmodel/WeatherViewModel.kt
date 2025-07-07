package com.example.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.WeatherInfo
import com.example.domain.repository.AppSettingsRepository
import com.example.presentation.uimodel.WeatherDisplayData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import jakarta.inject.Inject
import com.example.domain.usecase.SaveRecentCityUseCase
import com.example.domain.usecase.GetRecentCitiesUseCase
import com.example.domain.usecase.GetWeatherUseCase
import com.example.presentation.utils.CityNameResolver
import com.example.presentation.utils.LocationProvider
import com.example.presentation.utils.RecentCityUiMapper
import com.example.presentation.utils.WeatherUiMapper
import kotlinx.coroutines.flow.first

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val appSettingsRepository: AppSettingsRepository,
    private val saveRecentCityUseCase: SaveRecentCityUseCase,
    private val getRecentCitiesUseCase: GetRecentCitiesUseCase,
    private val weatherUiMapper: WeatherUiMapper,
    private val recentCityUiMapper: RecentCityUiMapper,
    private val locationProvider: LocationProvider,
    private val cityNameResolver: CityNameResolver
) : ViewModel() {

    private val _rawWeatherInfo = MutableStateFlow<WeatherInfo?>(null)

    val weatherDisplayData = _rawWeatherInfo
        .filterNotNull()
        .combine(appSettingsRepository.appSettingsFlow) { rawWeather, settings ->
            weatherUiMapper.map(rawWeather, settings)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _currentCity = MutableStateFlow("")
    val currentCity = _currentCity.asStateFlow()

    private val _requestLocationPermission = MutableStateFlow(false)
    val requestLocationPermission = _requestLocationPermission.asStateFlow()

    private val _recentCitiesDisplayData = MutableStateFlow<List<WeatherDisplayData>>(emptyList())
    val recentCitiesDisplayData = _recentCitiesDisplayData.asStateFlow()

    init {
        viewModelScope.launch {
            appSettingsRepository.appSettingsFlow.collect { settings ->
                if (settings.locationEnabled) {
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
        }

        viewModelScope.launch {
            getRecentCitiesUseCase().collect { recentCities ->
                val settings = appSettingsRepository.appSettingsFlow.first()
                _recentCitiesDisplayData.value = recentCityUiMapper.map(recentCities, settings)
            }
        }
    }

    fun searchWeather(cityName: String) {
        if (cityName.isNotBlank()) {
            _currentCity.value = cityName
            fetchWeatherByCityName(cityName)
        } else {
            _errorMessage.value = "City name cannot be empty."
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchCurrentLocationWeather() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _requestLocationPermission.value = false

            val location = locationProvider.getCurrentLocation()
            if (location != null) {
                val city = cityNameResolver.getCityName(location.latitude, location.longitude)
                if (city != null) {
                    _currentCity.value = city
                    fetchWeatherByCityName(city)
                } else {
                    _errorMessage.value = "Could not determine city from location."
                    fetchWeatherByCityName(_currentCity.value)
                }
            } else {
                _errorMessage.value = "Could not get location."
                fetchWeatherByCityName(_currentCity.value)
            }

            _isLoading.value = false
        }
    }

    private fun fetchWeatherByCityName(cityName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
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
                _errorMessage.value = if (msg.contains("not found", true)) null else "Failed to fetch: $msg"
                _rawWeatherInfo.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}

package com.example.presentation.viewmodel

// TODO: Main thing for now. ViewModel is too packed

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.WeatherForecast
import com.example.domain.model.WeatherInfo
import com.example.domain.usecase.GetAppSettingsUseCase
import com.example.domain.usecase.GetCityNameUseCase
import com.example.domain.usecase.GetCurrentLocationUseCase
import com.example.domain.usecase.GetForecastUseCase
import com.example.domain.usecase.GetRecentCitiesUseCase
import com.example.domain.usecase.GetWeatherUseCase
import com.example.domain.usecase.HasLocationPermissionUseCase
import com.example.domain.usecase.SaveRecentCityUseCase
import com.example.domain.usecase.conversion.ConvertTemperatureUseCase
import com.example.presentation.state.WeatherUiState
import com.example.presentation.viewmodel.utils.RecentCityUiMapper
import com.example.presentation.viewmodel.utils.WeatherDisplayData
import com.example.presentation.viewmodel.utils.WeatherUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val saveRecentCityUseCase: SaveRecentCityUseCase,
    private val getRecentCitiesUseCase: GetRecentCitiesUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val weatherUiMapper: WeatherUiMapper,
    private val recentCityUiMapper: RecentCityUiMapper,
    private val convertTemperature: ConvertTemperatureUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val hasLocationPermissionUseCase: HasLocationPermissionUseCase,
    private val getCityNameUseCase: GetCityNameUseCase
) : ViewModel() {

    private val _currentCity = MutableStateFlow("Belgrade")
    private val _uiState = MutableStateFlow<WeatherUiState<WeatherDisplayData>>(WeatherUiState.Empty)
    val uiState = _uiState.asStateFlow()
    private val _forecastState = MutableStateFlow<WeatherForecast?>(null)
    val forecastState = _forecastState.asStateFlow()
    private val _recentCitiesUiState = MutableStateFlow<WeatherUiState<List<WeatherDisplayData>>>(WeatherUiState.Empty)
    val recentCitiesUiState = _recentCitiesUiState.asStateFlow()
    private val searchTrigger = MutableSharedFlow<String>(replay = 1)
    private val _requestLocationPermission = MutableStateFlow(false)
    val requestLocationPermission = _requestLocationPermission.asStateFlow()

    private var latestWeatherInfo: WeatherInfo? = null

    init {
        observeAppSettings()
        observeRecentCities()

        viewModelScope.launch {
            searchTrigger
                .flatMapLatest { cityName ->
                    flow {
                        emit(WeatherUiState.Loading)
                        try {
                            val weatherInfo = getWeatherUseCase(cityName)
                            val forecast = getForecastUseCase(weatherInfo.latitude, weatherInfo.longitude)
                            val settings = getAppSettingsUseCase().first()
                            val displayData = weatherUiMapper.map(weatherInfo, settings)

                            val patchedDisplayData = forecast.daily.firstOrNull()?.let { day ->
                                displayData.copy(
                                    minTemperature = "Min: %.1f%s".format(
                                        convertTemperature(day.minTemp, settings.temperatureUnit),
                                        settings.temperatureUnit.label
                                    ),
                                    maxTemperature = "Max: %.1f%s".format(
                                        convertTemperature(day.maxTemp, settings.temperatureUnit),
                                        settings.temperatureUnit.label
                                    )
                                )
                            } ?: displayData


                            latestWeatherInfo = weatherInfo

                            _currentCity.value = weatherInfo.cityName
                            _forecastState.value = forecast
                            saveRecentCityUseCase(
                                cityName = weatherInfo.cityName,
                                temperature = weatherInfo.temperatureCelsius,
                                iconCode = weatherInfo.iconCode,
                                description = weatherInfo.description
                            )

                            emit(WeatherUiState.Success(patchedDisplayData))
                            Log.d("WeatherViewModel", "Updated UI with ${displayData.locationName}")

                        } catch (e: Exception) {
                            val msg = e.message ?: "Unknown error"
                            _forecastState.value = null
                            emit(WeatherUiState.Error("Failed to fetch weather for $cityName: $msg"))
                            Log.e("WeatherViewModel", "Error fetching weather", e)
                        }
                    }
                }
                .collect { state ->
                    _uiState.value = state
                }
        }

        viewModelScope.launch {
            getAppSettingsUseCase()
                .distinctUntilChanged()
                .collect { settings ->
                    latestWeatherInfo?.let { info ->
                        val displayData = weatherUiMapper.map(info, settings)
                        _uiState.value = WeatherUiState.Success(displayData)
                        Log.d("WeatherViewModel", "Re-mapped UI with new settings: ${settings.temperatureUnit}")
                    }
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
            if (hasLocationPermissionUseCase()) {
                fetchCurrentLocationWeather()
            } else {
                _requestLocationPermission.value = true
                searchWeather(_currentCity.value)
            }
        } else {
            searchWeather(_currentCity.value)
        }
    }

    fun searchWeather(cityName: String) {
        if (cityName.isNotBlank()) {
            viewModelScope.launch {
                searchTrigger.emit(cityName)
            }
        } else {
            _uiState.value = WeatherUiState.Error("City name cannot be empty.")
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchCurrentLocationWeather() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            _requestLocationPermission.value = false

            val location = getCurrentLocationUseCase()
            if (location != null) {
                val city = getCityNameUseCase(location.latitude, location.longitude)
                if (city != null) {
                    searchTrigger.emit(city)
                } else {
                    _uiState.value = WeatherUiState.Error("Could not determine city from location.")
                    searchTrigger.emit(_currentCity.value)
                }
            } else {
                _uiState.value = WeatherUiState.Error("Could not get location.")
                searchTrigger.emit(_currentCity.value)
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
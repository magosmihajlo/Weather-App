package com.example.presentation.viewmodel

// TODO: Main thing for now. ViewModel is too packed, I need to remove a lot of the code
// 90% of job is in here

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.DailyWeatherDisplayData
import com.example.domain.model.HourlyWeatherDisplayData
import com.example.domain.model.WeatherForecast
import com.example.domain.model.WeatherInfo
import com.example.domain.usecase.settings.GetAppSettingsUseCase
import com.example.domain.usecase.location.GetCityNameUseCase
import com.example.domain.usecase.location.GetCurrentLocationUseCase
import com.example.domain.usecase.api.GetForecastUseCase
import com.example.domain.usecase.database.GetRecentCitiesUseCase
import com.example.domain.usecase.api.GetWeatherUseCase
import com.example.domain.usecase.location.HasLocationPermissionUseCase
import com.example.domain.usecase.database.SaveRecentCityUseCase
import com.example.domain.usecase.settings.ConvertTemperatureUseCase
import com.example.presentation.state.WeatherUiState
import com.example.domain.model.WeatherDisplayData
import com.example.domain.usecase.display.MapDailyForecastUseCase
import com.example.domain.usecase.display.MapHourlyForecastUseCase
import com.example.domain.usecase.display.MapRecentCitiesUseCase
import com.example.domain.usecase.display.MapWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val saveRecentCityUseCase: SaveRecentCityUseCase,
    private val getRecentCitiesUseCase: GetRecentCitiesUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val mapRecentCitiesUseCase: MapRecentCitiesUseCase,
    private val convertTemperature: ConvertTemperatureUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val hasLocationPermissionUseCase: HasLocationPermissionUseCase,
    private val getCityNameUseCase: GetCityNameUseCase,
    private val mapper: MapWeatherUseCase,
    private val mapHourlyForecastUseCase: MapHourlyForecastUseCase,
    private val mapDailyForecastUseCase: MapDailyForecastUseCase,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState<WeatherDisplayData>>(WeatherUiState.Empty)
    val uiState = _uiState.asStateFlow()

    private val _forecastState = MutableStateFlow<WeatherForecast?>(null)
    val forecastState = _forecastState.asStateFlow()

    private val _hourlyDisplayState = MutableStateFlow<List<HourlyWeatherDisplayData>>(emptyList())
    val hourlyDisplayState = _hourlyDisplayState.asStateFlow()

    private val _dailyDisplayState = MutableStateFlow<List<DailyWeatherDisplayData>>(emptyList())
    val dailyDisplayState = _dailyDisplayState.asStateFlow()


    private val _recentCitiesUiState = MutableStateFlow<WeatherUiState<List<WeatherDisplayData>>>(WeatherUiState.Empty)
    val recentCitiesUiState = _recentCitiesUiState.asStateFlow()

    private val _requestLocationPermission = MutableStateFlow(false)
    val requestLocationPermission = _requestLocationPermission.asStateFlow()

    private val _currentCity = MutableStateFlow("Belgrade")
    private val searchTrigger = MutableSharedFlow<String>(replay = 1)

    private var latestWeatherInfo: WeatherInfo? = null

    private val settingsFlow = getAppSettingsUseCase().distinctUntilChanged().shareIn(viewModelScope, SharingStarted.Lazily, 1)

    init {
        observeSearchTrigger()
        observeRecentCities()
        observeAppSettings()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSearchTrigger() {
        viewModelScope.launch {
            searchTrigger
                .flatMapLatest { city -> loadWeather(city) }
                .collect { _uiState.value = it }
        }
    }

    private suspend fun loadWeather(city: String): Flow<WeatherUiState<WeatherDisplayData>> = flow {
        emit(WeatherUiState.Loading)
        try {
            val weatherInfo = getWeatherUseCase(city)
            val forecast = getForecastUseCase(weatherInfo.latitude, weatherInfo.longitude)
            val settings = settingsFlow.first()
            val displayData = mapper(weatherInfo, settings)

            val patchedData = forecast.daily.firstOrNull()?.let { day ->
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

            _hourlyDisplayState.value = mapHourlyForecastUseCase(
                forecast.hourly,
                weatherInfo.timezoneOffsetSeconds,
                settings
            )

            _dailyDisplayState.value = mapDailyForecastUseCase(
                forecast.daily,
                weatherInfo.timezoneOffsetSeconds,
                settings
            )




            saveRecentCityUseCase(
                weatherInfo.cityName,
                weatherInfo.temperatureCelsius,
                weatherInfo.iconCode,
                weatherInfo.description
            )

            emit(WeatherUiState.Success(patchedData))
        } catch (e: Exception) {
            _forecastState.value = null
            emit(WeatherUiState.Error("Failed to fetch weather for $city: ${e.message ?: "Unknown error"}"))
        }
    }

    private fun observeAppSettings() {
        viewModelScope.launch {
            settingsFlow.collect { settings ->
                latestWeatherInfo?.let {
                    _uiState.value = WeatherUiState.Success(mapper(it, settings))
                }

                forecastState.value?.let { forecast ->
                    _hourlyDisplayState.value = mapHourlyForecastUseCase(
                        forecast.hourly,
                        latestWeatherInfo?.timezoneOffsetSeconds ?: 0,
                        settings
                    )
                    _dailyDisplayState.value = mapDailyForecastUseCase(
                        forecast.daily,
                        latestWeatherInfo?.timezoneOffsetSeconds ?: 0,
                        settings
                    )
                }

            }
        }

        viewModelScope.launch {
            settingsFlow
                .map { it.locationEnabled }
                .distinctUntilChanged()
                .collect(::handleLocationEnabledChange)
        }
    }



    private fun observeRecentCities() {
        viewModelScope.launch {
            combine(
                getRecentCitiesUseCase(),
                getAppSettingsUseCase()
            ) { recentCities, settings -> recentCities to settings }
                .collect { (recentCities, settings) ->
                    _recentCitiesUiState.value = if (recentCities.isEmpty()) {
                        WeatherUiState.Empty
                    } else {
                        val displayData = mapRecentCitiesUseCase(recentCities, settings)
                        WeatherUiState.Success(displayData)
                    }
                }
        }
    }


    private fun handleLocationEnabledChange(enabled: Boolean) {
        if (enabled) {
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
        if (cityName.isBlank()) {
            _uiState.value = WeatherUiState.Error("City name cannot be empty.")
            return
        }

        viewModelScope.launch { searchTrigger.emit(cityName) }
    }

    @SuppressLint("MissingPermission")
    fun fetchCurrentLocationWeather() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            _requestLocationPermission.value = false

            val location = getCurrentLocationUseCase()
            val city = location?.let {
                getCityNameUseCase(it.latitude, it.longitude)
            }

            searchTrigger.emit(city ?: _currentCity.value)
        }
    }

    fun onLocationPermissionResult(granted: Boolean) {
        if (granted) fetchCurrentLocationWeather()
        else searchWeather("Belgrade")
    }
}

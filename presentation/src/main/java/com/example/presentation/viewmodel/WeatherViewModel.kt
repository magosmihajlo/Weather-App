package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AppSettings
import com.example.domain.model.DailyWeatherDisplayData
import com.example.domain.model.HourlyWeatherDisplayData
import com.example.domain.model.WeatherForecast
import com.example.domain.model.WeatherInfo
import com.example.domain.usecase.settings.GetAppSettingsUseCase
import com.example.domain.usecase.api.GetForecastUseCase
import com.example.domain.usecase.api.GetWeatherUseCase
import com.example.domain.usecase.database.SaveRecentCityUseCase
import com.example.presentation.state.WeatherUiState
import com.example.domain.model.WeatherDisplayData
import com.example.domain.usecase.display.MapDailyForecastUseCase
import com.example.domain.usecase.display.MapHourlyForecastUseCase
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
    private val getForecastUseCase: GetForecastUseCase,
    private val mapWeatherUseCase: MapWeatherUseCase,
    private val mapHourlyForecastUseCase: MapHourlyForecastUseCase,
    private val mapDailyForecastUseCase: MapDailyForecastUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState<WeatherDisplayData>>(WeatherUiState.Empty)
    val uiState: StateFlow<WeatherUiState<WeatherDisplayData>> = _uiState

    private val _forecastState = MutableStateFlow<WeatherForecast?>(null)
    val forecastState: StateFlow<WeatherForecast?> = _forecastState

    private val _hourlyDisplayState = MutableStateFlow<List<HourlyWeatherDisplayData>>(emptyList())
    val hourlyDisplayState: StateFlow<List<HourlyWeatherDisplayData>> = _hourlyDisplayState

    private val _dailyDisplayState = MutableStateFlow<List<DailyWeatherDisplayData>>(emptyList())
    val dailyDisplayState: StateFlow<List<DailyWeatherDisplayData>> = _dailyDisplayState

    private val _requestLocationPermission = MutableStateFlow(false)
    val requestLocationPermission: StateFlow<Boolean> = _requestLocationPermission

    private val _currentCity = MutableStateFlow("Belgrade")
    private val searchTrigger = MutableSharedFlow<String>(replay = 1)

    private var latestWeatherInfo: WeatherInfo? = null

    private val settingsFlow: SharedFlow<AppSettings> =
        getAppSettingsUseCase()
            .distinctUntilChanged()
            .shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)

    init {
        observeSearchTrigger()
        observeAppSettings()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSearchTrigger() {
        searchTrigger
            .flatMapLatest { city -> loadWeather(city) }
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }

    private fun loadWeather(city: String): Flow<WeatherUiState<WeatherDisplayData>> = flow {
        emit(WeatherUiState.Loading)
        try {
            val initialWeatherInfo = getWeatherUseCase(city)
            val forecast = getForecastUseCase(initialWeatherInfo.latitude, initialWeatherInfo.longitude)

            val todayForecast = forecast.daily.firstOrNull()

            val weatherInfo = if (todayForecast != null) {
                initialWeatherInfo.copy(
                    minTemperatureCelsius = todayForecast.minTemp,
                    maxTemperatureCelsius = todayForecast.maxTemp
                )
            } else {
                initialWeatherInfo
            }

            val settings = settingsFlow.first()
            val displayData = mapWeatherUseCase(weatherInfo, settings)

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

            emit(WeatherUiState.Success(displayData))
        } catch (e: Exception) {
            _forecastState.value = null
            emit(WeatherUiState.Error("Failed to fetch weather for $city: ${e.message ?: "Unknown error"}"))
        }
    }

    private fun observeAppSettings() {
        settingsFlow
            .onEach { settings ->
                latestWeatherInfo?.let {
                    _uiState.value = WeatherUiState.Success(mapWeatherUseCase(it, settings))
                }
                forecastState.value?.let { forecast ->
                    val timezone = latestWeatherInfo?.timezoneOffsetSeconds ?: 0
                    _hourlyDisplayState.value = mapHourlyForecastUseCase(forecast.hourly, timezone, settings)
                    _dailyDisplayState.value = mapDailyForecastUseCase(forecast.daily, timezone, settings)
                }
            }
            .launchIn(viewModelScope)
    }





    fun searchWeather(cityName: String) {
        if (cityName.isBlank()) {
            _uiState.value = WeatherUiState.Error("City name cannot be empty.")
            return
        }

        viewModelScope.launch { searchTrigger.emit(cityName) }
    }


}

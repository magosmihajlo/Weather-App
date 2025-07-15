package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AppSettings
import com.example.domain.model.DailyWeatherDisplayData
import com.example.domain.model.HourlyWeatherDisplayData
import com.example.domain.model.WeatherForecast
import com.example.domain.model.WeatherInfo
import com.example.domain.usecase.display.MapDailyForecastUseCase
import com.example.domain.usecase.display.MapHourlyForecastUseCase
import com.example.domain.usecase.settings.GetAppSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    getAppSettingsUseCase: GetAppSettingsUseCase,
    private val mapHourlyForecastUseCase: MapHourlyForecastUseCase,
    private val mapDailyForecastUseCase: MapDailyForecastUseCase,
) : ViewModel() {

    private val _hourlyDisplayState = MutableStateFlow<List<HourlyWeatherDisplayData>>(emptyList())
    val hourlyDisplayState: StateFlow<List<HourlyWeatherDisplayData>> = _hourlyDisplayState

    private val _dailyDisplayState = MutableStateFlow<List<DailyWeatherDisplayData>>(emptyList())
    val dailyDisplayState: StateFlow<List<DailyWeatherDisplayData>> = _dailyDisplayState

    private val settingsFlow: SharedFlow<AppSettings> = getAppSettingsUseCase()
        .distinctUntilChanged()
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000), replay = 1)


    private var latestForecast: WeatherForecast? = null
    private var latestWeatherInfo: WeatherInfo? = null

    private fun updateMappedForecast(forecast: WeatherForecast, weatherInfo: WeatherInfo) {
        viewModelScope.launch {
            val settings = settingsFlow.first()
            _hourlyDisplayState.value = mapHourlyForecastUseCase(
                forecast.hourly, weatherInfo.timezoneOffsetSeconds, settings
            )
            _dailyDisplayState.value = mapDailyForecastUseCase(
                forecast.daily, weatherInfo.timezoneOffsetSeconds, settings
            )
        }
    }

    init {
        settingsFlow.onEach { settings ->
            latestForecast?.let { forecast ->
                latestWeatherInfo?.let { info ->
                    updateDisplayData(forecast, info, settings)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateForecast(forecast: WeatherForecast, weatherInfo: WeatherInfo) {
        latestForecast = forecast
        latestWeatherInfo = weatherInfo
        viewModelScope.launch {
            val settings = settingsFlow.first()
            updateDisplayData(forecast, weatherInfo, settings)
        }
    }

    fun clearForecast() {
        latestForecast = null
        latestWeatherInfo = null
        _hourlyDisplayState.value = emptyList()
        _dailyDisplayState.value = emptyList()
    }

    private fun updateDisplayData(forecast: WeatherForecast, info: WeatherInfo, settings: AppSettings) {
        _hourlyDisplayState.value = mapHourlyForecastUseCase(
            forecast.hourly, info.timezoneOffsetSeconds, settings
        )
        _dailyDisplayState.value = mapDailyForecastUseCase(
            forecast.daily, info.timezoneOffsetSeconds, settings
        )
    }
}

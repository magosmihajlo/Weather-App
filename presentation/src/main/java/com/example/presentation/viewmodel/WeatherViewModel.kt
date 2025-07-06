package com.example.presentation.viewmodel

// TODO: I put too much stuff in one place. Separate code and check for possible reusability improvements
// TODO: Try to incorporate SOLID principle

import android.annotation.SuppressLint
import android.app.Application
import android.location.Geocoder
import android.location.Address
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AppSettings
import com.example.domain.model.TimeFormat
import com.example.domain.repository.AppSettingsRepository
import com.example.domain.repository.WeatherRepository
import com.example.domain.usecase.ConvertPressureUseCase
import com.example.domain.usecase.ConvertTemperatureUseCase
import com.example.domain.usecase.ConvertWindSpeedUseCase
import com.example.presentation.uimodel.WeatherDisplayData
import com.example.presentation.uimodel.hourMinuteFormatter12
import com.example.presentation.uimodel.hourMinuteFormatter24
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import com.example.domain.usecase.SaveRecentCityUseCase
import com.example.domain.usecase.GetRecentCitiesUseCase
import kotlinx.coroutines.flow.first

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val appSettingsRepository: AppSettingsRepository,
    private val convertTemperatureUseCase: ConvertTemperatureUseCase,
    private val convertWindSpeedUseCase: ConvertWindSpeedUseCase,
    private val convertPressureUseCase: ConvertPressureUseCase,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
    private val application: Application,
    private val saveRecentCityUseCase: SaveRecentCityUseCase,
    private val getRecentCitiesUseCase: GetRecentCitiesUseCase
) : ViewModel() {

    private val _rawWeatherInfo = MutableStateFlow<com.example.domain.model.WeatherInfo?>(null)

    val weatherDisplayData: StateFlow<WeatherDisplayData?> =
        _rawWeatherInfo
            .filterNotNull()
            .distinctUntilChanged()
            .combine(appSettingsRepository.appSettingsFlow.distinctUntilChanged()) { rawWeather, settings ->
                mapToWeatherDisplayData(rawWeather, settings)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _currentCity = MutableStateFlow("") // TODO: Default initial city. I should fix that
    val currentCity: StateFlow<String> = _currentCity.asStateFlow()

    private val _requestLocationPermission = MutableStateFlow(false)
    val requestLocationPermission: StateFlow<Boolean> = _requestLocationPermission.asStateFlow()

    private val _recentCitiesDisplayData = MutableStateFlow<List<WeatherDisplayData>>(emptyList())
    val recentCitiesDisplayData: StateFlow<List<WeatherDisplayData>> = _recentCitiesDisplayData.asStateFlow()

    private val _appSettings = appSettingsRepository.appSettingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    init {
        viewModelScope.launch {
            _appSettings.collect { settings ->
                if (settings.locationEnabled) {
                    if (hasLocationPermission(application)) {
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
                val settings = _appSettings.first()
                _recentCitiesDisplayData.value = recentCities.map { domainCity ->
                    WeatherDisplayData(
                        locationName = domainCity.cityName,
                        description = domainCity.description,
                        iconUrl = "https://openweathermap.org/img/wn/${domainCity.iconCode}@2x.png",
                        temperature = String.format(Locale.getDefault(), "%.1f%s", convertTemperatureUseCase(domainCity.temperature, settings.temperatureUnit), settings.temperatureUnit.label),
                        minTemperature = "",
                        maxTemperature = "",
                        feelsLikeTemperature = "",
                        humidity = "",
                        windSpeed = "",
                        pressure = "",
                        visibility = "",
                        sunriseTime = "",
                        sunsetTime = ""
                    )
                }
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

            try {
                val locationResult = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).await()

                if (locationResult != null) {
                    val latitude = locationResult.latitude
                    val longitude = locationResult.longitude
                    val cityName = getCityNameFromCoordinates(latitude, longitude)

                    if (cityName != null) {
                        _currentCity.value = cityName
                        fetchWeatherByCityName(cityName)
                    } else {
                        _errorMessage.value = "Could not determine city name from location. Displaying last known city."
                        fetchWeatherByCityName(_currentCity.value)
                    }
                } else {
                    _errorMessage.value = "Could not get current location. Ensure GPS is enabled. Displaying last known city."
                    fetchWeatherByCityName(_currentCity.value)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error getting location: ${e.message}. Displaying last known city."
                fetchWeatherByCityName(_currentCity.value)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun hasLocationPermission(context: Application): Boolean {
        return androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED ||
                androidx.core.content.ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun getCityNameFromCoordinates(latitude: Double, longitude: Double): String? {
        return try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.firstOrNull()?.locality
        } catch (e: Exception) {
            _errorMessage.value = "Geocoding error: ${e.message}"
            null
        }
    }

    private fun fetchWeatherByCityName(cityName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val weatherInfo = weatherRepository.getCurrentWeather(cityName)
                _rawWeatherInfo.value = weatherInfo
                _currentCity.value = cityName

                saveRecentCityUseCase(
                    cityName = weatherInfo.cityName,
                    temperature = weatherInfo.temperatureCelsius,
                    iconCode = weatherInfo.iconCode,
                    description = weatherInfo.description
                )

            } catch (e: Exception) {
                val errorMessageContent = e.message ?: "Unknown error"
                if (errorMessageContent.contains("400 Bad Request", ignoreCase = true) ||
                    errorMessageContent.contains("city not found", ignoreCase = true) ||
                    errorMessageContent.contains("not found", ignoreCase = true)
                ) {
                    _errorMessage.value = null
                    _rawWeatherInfo.value = null
                    _currentCity.value = ""
                } else {
                    _errorMessage.value = "Failed to fetch weather for $cityName: $errorMessageContent"
                    _rawWeatherInfo.value = null
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun mapToWeatherDisplayData(
        weatherInfo: com.example.domain.model.WeatherInfo,
        settings: AppSettings
    ): WeatherDisplayData {
        val currentTemp = convertTemperatureUseCase(weatherInfo.temperatureCelsius, settings.temperatureUnit)
        val minTemp = convertTemperatureUseCase(weatherInfo.minTemperatureCelsius, settings.temperatureUnit)
        val maxTemp = convertTemperatureUseCase(weatherInfo.maxTemperatureCelsius, settings.temperatureUnit)
        val feelsLikeTemp = convertTemperatureUseCase(weatherInfo.feelsLikeCelsius, settings.temperatureUnit)
        val windSpeed = convertWindSpeedUseCase(weatherInfo.windSpeedKmh, settings.windSpeedUnit)
        val pressure = convertPressureUseCase(weatherInfo.pressureHPa.toDouble(), settings.pressureUnit)
        val humidity = weatherInfo.humidityPercent

        val iconUrl = "https://openweathermap.org/img/wn/${weatherInfo.iconCode}@2x.png"

        val timeFormatter = if (settings.timeFormat == TimeFormat.TWELVE_HOUR) {
            hourMinuteFormatter12
        } else {
            hourMinuteFormatter24
        }

        val sunriseInstant = Instant.ofEpochSecond(weatherInfo.sunriseTime)
        val sunsetInstant = Instant.ofEpochSecond(weatherInfo.sunsetTime)

        return WeatherDisplayData(
            locationName = weatherInfo.cityName,
            description = weatherInfo.description,
            iconUrl = iconUrl,
            temperature = String.format(Locale.getDefault(), "%.1f%s", currentTemp, settings.temperatureUnit.label),
            minTemperature = String.format(Locale.getDefault(), "Min: %.1f%s", minTemp, settings.temperatureUnit.label),
            maxTemperature = String.format(Locale.getDefault(), "Max: %.1f%s", maxTemp, settings.temperatureUnit.label),
            feelsLikeTemperature = String.format(Locale.getDefault(), "Feels Like: %.1f%s", feelsLikeTemp, settings.temperatureUnit.label),
            humidity = String.format(Locale.getDefault(), "%d%%", humidity),
            windSpeed = String.format(Locale.getDefault(), "%.1f %s", windSpeed, settings.windSpeedUnit.label),
            pressure = String.format(Locale.getDefault(), "%.0f %s", pressure, settings.pressureUnit.label),
            visibility = String.format(Locale.getDefault(), "%.0f km", weatherInfo.visibilityMeters / 1000.0),
            sunriseTime = formatTime(sunriseInstant, timeFormatter),
            sunsetTime = formatTime(sunsetInstant, timeFormatter)
        )
    }

    private fun formatTime(instant: Instant, formatter: DateTimeFormatter): String {
        return instant.atZone(ZoneId.systemDefault()).format(formatter)
    }

}
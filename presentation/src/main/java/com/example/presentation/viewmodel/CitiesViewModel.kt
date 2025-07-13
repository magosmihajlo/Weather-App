package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.WeatherDisplayData
import com.example.domain.usecase.UpdateCitiesUseCase
import com.example.domain.usecase.database.GetRecentCitiesUseCase
import com.example.domain.usecase.display.MapRecentCitiesUseCase
import com.example.domain.usecase.settings.GetAppSettingsUseCase
import com.example.presentation.state.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val getRecentCitiesUseCase: GetRecentCitiesUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val mapRecentCitiesUseCase: MapRecentCitiesUseCase,
    private val updateCitiesUseCase: UpdateCitiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState<List<WeatherDisplayData>>>(WeatherUiState.Empty)
    val uiState: StateFlow<WeatherUiState<List<WeatherDisplayData>>> = _uiState

    init {
        refreshAndObserveCities()
    }

    private fun refreshAndObserveCities() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            try {
                updateCitiesUseCase()
                observeRecentCities()

            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message ?: "Failed to update cities.")
            }
        }
    }

    private fun observeRecentCities() {
        getRecentCitiesUseCase()
            .combine(getAppSettingsUseCase()) { cities, settings ->
                cities to settings
            }
            .onEach { (cities, settings) ->
                _uiState.value = if (cities.isEmpty()) {
                    WeatherUiState.Empty
                } else {
                    WeatherUiState.Success(mapRecentCitiesUseCase(cities, settings))
                }
            }
            .launchIn(viewModelScope)
    }
}

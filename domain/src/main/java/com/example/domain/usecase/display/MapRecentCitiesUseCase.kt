package com.example.domain.usecase.display

import com.example.domain.model.AppSettings
import com.example.domain.model.RecentCity
import com.example.domain.model.WeatherDisplayData
import com.example.domain.repository.display.RecentCityDisplayRepository
import javax.inject.Inject

class MapRecentCitiesUseCase @Inject constructor(
    private val repository: RecentCityDisplayRepository
) {
    operator fun invoke(cities: List<RecentCity>, settings: AppSettings): List<WeatherDisplayData> {
        return repository.mapToDisplay(cities, settings)
    }
}
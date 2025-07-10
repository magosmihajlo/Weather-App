package com.example.domain.usecase

import com.example.domain.model.AppSettings
import com.example.domain.model.RecentCity
import com.example.domain.model.WeatherDisplayData
import com.example.domain.repository.RecentCityDisplayRepository
import javax.inject.Inject

class MapRecentCitiesToDisplayUseCase @Inject constructor(
    private val repository: RecentCityDisplayRepository
) {
    operator fun invoke(cities: List<RecentCity>, settings: AppSettings): List<WeatherDisplayData> {
        return repository.mapToDisplay(cities, settings)
    }
}

package com.example.domain.repository.display

import com.example.domain.model.AppSettings
import com.example.domain.model.RecentCity
import com.example.domain.model.WeatherDisplayData

interface RecentCityDisplayRepository {
    fun mapToDisplay(cities: List<RecentCity>, settings: AppSettings): List<WeatherDisplayData>
}

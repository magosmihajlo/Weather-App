package com.example.domain.usecase.database

import com.example.domain.repository.database.RecentCityRepository
import javax.inject.Inject

class SaveRecentCityUseCase @Inject constructor(
    private val recentCityRepository: RecentCityRepository
) {
    suspend operator fun invoke(cityName: String, temperature: Double, iconCode: String, description: String) {
        recentCityRepository.saveRecentCity(cityName, temperature, iconCode, description)
    }
}
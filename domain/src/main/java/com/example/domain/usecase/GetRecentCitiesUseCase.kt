package com.example.domain.usecase

import com.example.domain.model.RecentCity
import com.example.domain.repository.RecentCityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentCitiesUseCase @Inject constructor(
    private val recentCityRepository: RecentCityRepository
) {
    operator fun invoke(): Flow<List<RecentCity>> {
        return recentCityRepository.getRecentCities()
    }
}
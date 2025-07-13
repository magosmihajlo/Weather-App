package com.example.domain.usecase

import com.example.domain.repository.database.UpdateCitiesRepository
import javax.inject.Inject

class UpdateCitiesUseCase @Inject constructor(
    private val updateCitiesRepository: UpdateCitiesRepository
) {
    suspend operator fun invoke() {
        updateCitiesRepository.updateCitiesWeather()
    }
}
package com.example.domain.usecase

import com.example.domain.repository.LocationProvider
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationProvider: LocationProvider
) {
    suspend operator fun invoke() = locationProvider.getCurrentLocation()
}

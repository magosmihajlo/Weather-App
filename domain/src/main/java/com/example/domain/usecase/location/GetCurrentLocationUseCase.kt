package com.example.domain.usecase.location

import com.example.domain.repository.location.LocationProvider
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationProvider: LocationProvider
) {
    suspend operator fun invoke() = locationProvider.getCurrentLocation()
}
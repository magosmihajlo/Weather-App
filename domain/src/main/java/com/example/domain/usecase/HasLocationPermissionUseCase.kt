package com.example.domain.usecase

import com.example.domain.repository.LocationProvider
import javax.inject.Inject

class HasLocationPermissionUseCase @Inject constructor(
    private val locationProvider: LocationProvider
) {
    operator fun invoke() = locationProvider.hasLocationPermission()
}

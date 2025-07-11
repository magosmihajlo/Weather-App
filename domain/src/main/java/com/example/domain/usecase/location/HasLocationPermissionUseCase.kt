package com.example.domain.usecase.location

import com.example.domain.repository.location.LocationProvider
import javax.inject.Inject

class HasLocationPermissionUseCase @Inject constructor(
    private val locationProvider: LocationProvider
) {
    operator fun invoke() = locationProvider.hasLocationPermission()
}
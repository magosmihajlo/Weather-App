package com.example.domain.repository

import android.location.Location

interface LocationProvider {
    fun hasLocationPermission(): Boolean
    suspend fun getCurrentLocation(): Location?
}

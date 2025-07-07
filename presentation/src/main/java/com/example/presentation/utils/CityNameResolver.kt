package com.example.presentation.utils

import android.location.Geocoder
import jakarta.inject.Inject

class CityNameResolver @Inject constructor(
    private val geocoder: Geocoder
) {
    fun getCityName(lat: Double, lon: Double): String? {
        return try {
            geocoder.getFromLocation(lat, lon, 1)?.firstOrNull()?.locality
        } catch (e: Exception) {
            null
        }
    }
}

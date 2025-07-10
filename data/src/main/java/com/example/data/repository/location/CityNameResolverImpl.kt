package com.example.data.repository.location

import android.location.Geocoder
import com.example.domain.repository.CityNameResolver
import javax.inject.Inject

class CityNameResolverImpl @Inject constructor(
    private val geocoder: Geocoder
) : CityNameResolver {

    override fun getCityName(lat: Double, lon: Double): String? {
        return try {
            geocoder.getFromLocation(lat, lon, 1)?.firstOrNull()?.locality
        } catch (e: Exception) {
            null
        }
    }
}

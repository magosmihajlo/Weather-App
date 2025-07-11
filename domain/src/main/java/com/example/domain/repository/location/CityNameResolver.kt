package com.example.domain.repository.location

interface CityNameResolver {
    fun getCityName(lat: Double, lon: Double): String?
}

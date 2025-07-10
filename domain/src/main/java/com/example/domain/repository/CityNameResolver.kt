package com.example.domain.repository

interface CityNameResolver {
    fun getCityName(lat: Double, lon: Double): String?
}

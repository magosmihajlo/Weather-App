package com.example.domain.repository.database

import com.example.domain.model.RecentCity
import kotlinx.coroutines.flow.Flow

interface RecentCityRepository {
    suspend fun saveRecentCity(cityName: String, temperature: Double, iconCode: String, description: String)
    fun getRecentCities(): Flow<List<RecentCity>>
}
package com.example.data.repository.general

import com.example.data.local.dao.RecentCityDao
import com.example.data.local.entity.RecentCityEntity
import com.example.data.mapper.toDomain
import com.example.domain.model.RecentCity
import com.example.domain.repository.RecentCityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentCityRepositoryImpl @Inject constructor(
    private val recentCityDao: RecentCityDao
) : RecentCityRepository {

    override suspend fun saveRecentCity(
        cityName: String,
        temperature: Double,
        iconCode: String,
        description: String
    ) {
        val entity = RecentCityEntity(
            cityName = cityName,
            temperature = temperature,
            iconCode = iconCode,
            description = description,
            lastAccessed = System.currentTimeMillis()
        )
        recentCityDao.insertRecentCity(entity)
        recentCityDao.pruneOldCities()
    }

    override fun getRecentCities(): Flow<List<RecentCity>> {
        return recentCityDao.getRecentCities().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
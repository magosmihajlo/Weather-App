package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.RecentCityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentCityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentCity(city: RecentCityEntity)

    @Query("SELECT * FROM recent_cities ORDER BY lastAccessed DESC LIMIT 5")
    fun getRecentCities(): Flow<List<RecentCityEntity>>

    @Query("DELETE FROM recent_cities WHERE cityName = :cityName")
    suspend fun deleteCityByName(cityName: String)

    @Query("DELETE FROM recent_cities WHERE cityName NOT IN (SELECT cityName FROM recent_cities ORDER BY lastAccessed DESC LIMIT 5)")
    suspend fun pruneOldCities()
}
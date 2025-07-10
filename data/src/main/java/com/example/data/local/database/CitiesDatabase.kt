package com.example.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.RecentCityDao
import com.example.data.local.entity.RecentCityEntity

@Database(entities = [RecentCityEntity::class], version = 1, exportSchema = false)
abstract class CitiesDatabase : RoomDatabase() {
    abstract fun recentCityDao(): RecentCityDao
}
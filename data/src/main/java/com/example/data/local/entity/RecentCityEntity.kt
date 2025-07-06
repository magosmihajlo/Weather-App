package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_cities")
data class RecentCityEntity(
    @PrimaryKey
    val cityName: String,
    val temperature: Double,
    val iconCode: String,
    val description: String,
    val lastAccessed: Long
)
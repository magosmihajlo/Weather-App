package com.example.domain.model

data class RecentCity(
    val cityName: String,
    val temperature: Double,
    val iconCode: String,
    val description: String,
    val lastAccessed: Long
)
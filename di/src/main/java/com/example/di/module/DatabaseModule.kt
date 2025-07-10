package com.example.di.module

import android.app.Application
import androidx.room.Room
import com.example.data.local.dao.RecentCityDao
import com.example.data.local.database.CitiesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): CitiesDatabase {
        return Room.databaseBuilder(
            application,
            CitiesDatabase::class.java,
            "weather_app_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecentCityDao(citiesDatabase: CitiesDatabase): RecentCityDao {
        return citiesDatabase.recentCityDao()
    }
}
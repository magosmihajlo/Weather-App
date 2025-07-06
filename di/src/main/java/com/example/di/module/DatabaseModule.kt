package com.example.di.module

import android.app.Application
import androidx.room.Room
import com.example.data.local.dao.RecentCityDao
import com.example.data.local.database.AppDatabase
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
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "weather_app_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecentCityDao(appDatabase: AppDatabase): RecentCityDao {
        return appDatabase.recentCityDao()
    }
}
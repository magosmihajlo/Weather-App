package com.example.di.module

import com.example.data.repository.WeatherRepositoryImpl
import com.example.data.repository.RecentCityRepositoryImpl
import com.example.data.repository.UnitConversionRepositoryImpl
import com.example.domain.repository.RecentCityRepository
import com.example.domain.repository.UnitConversionRepository
import com.example.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindRecentCityRepository(
        recentCityRepositoryImpl: RecentCityRepositoryImpl
    ): RecentCityRepository

    @Binds
    @Singleton
    abstract fun bindUnitConversionRepository(
        impl: UnitConversionRepositoryImpl
    ): UnitConversionRepository
}

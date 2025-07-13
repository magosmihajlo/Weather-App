package com.example.di.module

import android.app.Application
import android.location.Geocoder
import com.example.data.repository.display.ForecastDisplayRepositoryImpl
import com.example.data.repository.display.RecentCityDisplayRepositoryImpl
import com.example.data.repository.general.WeatherRepositoryImpl
import com.example.data.repository.general.RecentCityRepositoryImpl
import com.example.data.repository.settings.UnitConversionRepositoryImpl
import com.example.data.repository.display.WeatherDisplayRepositoryImpl
import com.example.data.repository.general.UpdateCitiesRepositoryImpl
import com.example.data.repository.location.CityNameResolverImpl
import com.example.data.repository.location.LocationProviderImpl
import com.example.data.repository.settings.AppSettingsRepositoryImpl
import com.example.domain.repository.settings.AppSettingsRepository
import com.example.domain.repository.location.CityNameResolver
import com.example.domain.repository.display.ForecastDisplayRepository
import com.example.domain.repository.location.LocationProvider
import com.example.domain.repository.display.RecentCityDisplayRepository
import com.example.domain.repository.database.RecentCityRepository
import com.example.domain.repository.settings.UnitConversionRepository
import com.example.domain.repository.display.WeatherDisplayRepository
import com.example.domain.repository.api.WeatherRepository
import com.example.domain.repository.database.UpdateCitiesRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Locale
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

    @Binds
    @Singleton
    abstract fun bindWeatherDisplayRepository(
        impl: WeatherDisplayRepositoryImpl
    ): WeatherDisplayRepository

    @Binds
    @Singleton
    abstract fun bindRecentCityDisplayRepository(
        impl: RecentCityDisplayRepositoryImpl
    ): RecentCityDisplayRepository

    @Binds
    @Singleton
    abstract fun bindAppSettingsRepository(
        impl: AppSettingsRepositoryImpl
    ): AppSettingsRepository

    @Binds
    @Singleton
    abstract fun bindLocationProvider(
        impl: LocationProviderImpl
    ): LocationProvider

    @Binds
    @Singleton
    abstract fun bindCityNameResolver(
        impl: CityNameResolverImpl
    ): CityNameResolver

    companion object {

        @Provides
        @Singleton
        fun provideFusedLocationProviderClient(application: Application): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(application)
        }

        @Provides
        @Singleton
        fun provideGeocoder(application: Application): Geocoder {
            return Geocoder(application, Locale.getDefault())
        }
    }

    @Binds
    @Singleton
    abstract fun bindForecastDisplayRepository(
        impl: ForecastDisplayRepositoryImpl
    ): ForecastDisplayRepository

    @Binds
    @Singleton
    abstract fun bindUpdateCitiesRepository(
        impl: UpdateCitiesRepositoryImpl
    ): UpdateCitiesRepository

}

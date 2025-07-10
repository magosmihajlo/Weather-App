package com.example.di.module

import com.example.domain.repository.AppSettingsRepository
import com.example.domain.repository.CityNameResolver
import com.example.domain.repository.LocationProvider
import com.example.domain.repository.RecentCityDisplayRepository
import com.example.domain.repository.RecentCityRepository
import com.example.domain.repository.UnitConversionRepository
import com.example.domain.repository.WeatherDisplayRepository
import com.example.domain.repository.WeatherRepository
import com.example.domain.usecase.conversion.ConvertPressureUseCase
import com.example.domain.usecase.conversion.ConvertTemperatureUseCase
import com.example.domain.usecase.conversion.ConvertWindSpeedUseCase
import com.example.domain.usecase.GetAppSettingsUseCase
import com.example.domain.usecase.GetCityNameUseCase
import com.example.domain.usecase.GetCurrentLocationUseCase
import com.example.domain.usecase.GetRecentCitiesUseCase
import com.example.domain.usecase.GetWeatherUseCase
import com.example.domain.usecase.MapRecentCitiesToDisplayUseCase
import com.example.domain.usecase.MapWeatherToDisplayUseCase
import com.example.domain.usecase.SaveRecentCityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetWeatherUseCase(
        repository: WeatherRepository
    ): GetWeatherUseCase = GetWeatherUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveRecentCityUseCase(
        recentCityRepository: RecentCityRepository
    ): SaveRecentCityUseCase {
        return SaveRecentCityUseCase(recentCityRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecentCitiesUseCase(
        recentCityRepository: RecentCityRepository
    ): GetRecentCitiesUseCase {
        return GetRecentCitiesUseCase(recentCityRepository)
    }

    @Provides
    @Singleton
    fun provideConvertTemperatureUseCase(
        repo: UnitConversionRepository
    ): ConvertTemperatureUseCase = ConvertTemperatureUseCase(repo)

    @Provides
    @Singleton
    fun provideConvertWindSpeedUseCase(
        repo: UnitConversionRepository
    ): ConvertWindSpeedUseCase = ConvertWindSpeedUseCase(repo)

    @Provides
    @Singleton
    fun provideConvertPressureUseCase(
        repo: UnitConversionRepository
    ): ConvertPressureUseCase = ConvertPressureUseCase(repo)

    @Provides
    @Singleton
    fun provideGetAppSettingsUseCase(
        appSettingsRepository: AppSettingsRepository
    ): GetAppSettingsUseCase = GetAppSettingsUseCase(appSettingsRepository)


    @Provides
    @Singleton
    fun provideGetCurrentLocationUseCase(
        locationProvider: LocationProvider
    ): GetCurrentLocationUseCase = GetCurrentLocationUseCase(locationProvider)

    @Provides
    @Singleton
    fun provideGetCityNameUseCase(
        cityNameResolver: CityNameResolver
    ): GetCityNameUseCase = GetCityNameUseCase(cityNameResolver)


    @Provides
    @Singleton
    fun provideMapWeatherToDisplayUseCase(
        weatherDisplayRepository: WeatherDisplayRepository
    ): MapWeatherToDisplayUseCase {
        return MapWeatherToDisplayUseCase(weatherDisplayRepository)
    }

    @Provides
    @Singleton
    fun provideMapRecentCitiesToDisplayUseCase(
        repository: RecentCityDisplayRepository
    ): MapRecentCitiesToDisplayUseCase = MapRecentCitiesToDisplayUseCase(repository)


}





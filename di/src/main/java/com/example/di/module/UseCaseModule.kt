package com.example.di.module

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
import com.example.domain.usecase.UpdateCitiesUseCase
import com.example.domain.usecase.settings.ConvertPressureUseCase
import com.example.domain.usecase.settings.ConvertTemperatureUseCase
import com.example.domain.usecase.settings.ConvertWindSpeedUseCase
import com.example.domain.usecase.settings.GetAppSettingsUseCase
import com.example.domain.usecase.location.GetCityNameUseCase
import com.example.domain.usecase.location.GetCurrentLocationUseCase
import com.example.domain.usecase.database.GetRecentCitiesUseCase
import com.example.domain.usecase.api.GetWeatherUseCase
import com.example.domain.usecase.display.MapDailyForecastUseCase
import com.example.domain.usecase.display.MapHourlyForecastUseCase
import com.example.domain.usecase.display.MapRecentCitiesUseCase
import com.example.domain.usecase.display.MapWeatherUseCase
import com.example.domain.usecase.database.SaveRecentCityUseCase
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
    ): MapWeatherUseCase {
        return MapWeatherUseCase(weatherDisplayRepository)
    }

    @Provides
    @Singleton
    fun provideMapRecentCitiesToDisplayUseCase(
        repository: RecentCityDisplayRepository
    ): MapRecentCitiesUseCase = MapRecentCitiesUseCase(repository)


    @Provides
    @Singleton
    fun provideMapHourlyForecastToDisplayUseCase(
        repository: ForecastDisplayRepository
    ): MapHourlyForecastUseCase = MapHourlyForecastUseCase(repository)

    @Provides
    @Singleton
    fun provideMapDailyForecastToDisplayUseCase(
        repository: ForecastDisplayRepository
    ): MapDailyForecastUseCase = MapDailyForecastUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateCitiesUseCase(
        updateCitiesRepository: UpdateCitiesRepository
    ): UpdateCitiesUseCase = UpdateCitiesUseCase(updateCitiesRepository)

}





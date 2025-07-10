package com.example.di.module

import android.app.Application
import android.location.Geocoder
import com.example.domain.repository.CityNameResolver
import com.example.domain.repository.LocationProvider
import com.example.data.repository.CityNameResolverImpl
import com.example.data.repository.LocationProviderImpl
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
abstract class DataModule {

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
}

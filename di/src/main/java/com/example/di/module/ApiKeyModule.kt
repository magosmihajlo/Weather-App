package com.example.di.module

import com.example.di.DiConfigurator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppApiKeyModule {

    @Provides
    @Singleton
    @Named("api_key")
    fun provideApiKey(diConfigurator: DiConfigurator): String {
        return diConfigurator.getWeatherApiKey()
    }
}
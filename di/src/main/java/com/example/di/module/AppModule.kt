package com.example.di.module

import com.example.data.manager.SettingsManager
import com.example.domain.repository.AppSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAppSettingsRepository(
        settingsManager: SettingsManager
    ): AppSettingsRepository
}
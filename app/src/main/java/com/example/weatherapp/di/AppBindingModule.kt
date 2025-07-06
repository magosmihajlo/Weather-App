package com.example.weatherapp.di

import com.example.di.DiConfigurator
import com.example.weatherapp.AppConfigurator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingModule {

    @Binds
    @Singleton
    abstract fun bindConfigurationProvider(
        appConfigurator: AppConfigurator
    ): DiConfigurator
}
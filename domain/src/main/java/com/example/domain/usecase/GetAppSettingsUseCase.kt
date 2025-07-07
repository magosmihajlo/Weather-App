package com.example.domain.usecase

import com.example.domain.model.AppSettings
import com.example.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppSettingsUseCase @Inject constructor(
    private val repository: AppSettingsRepository
) {
    operator fun invoke(): Flow<AppSettings> = repository.appSettingsFlow
}

package com.example.domain.usecase.settings

import com.example.domain.repository.settings.AppSettingsRepository
import javax.inject.Inject

class UpdateLocationEnabledUseCase @Inject constructor(
    private val repository: AppSettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) = repository.updateLocationEnabled(enabled)
}

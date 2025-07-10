package com.example.domain.usecase

import com.example.domain.repository.CityNameResolver
import javax.inject.Inject

class GetCityNameUseCase @Inject constructor(
    private val resolver: CityNameResolver
) {
    suspend operator fun invoke(lat: Double, lon: Double): String? {
        return resolver.getCityName(lat, lon)
    }
}

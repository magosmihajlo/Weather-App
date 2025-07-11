package com.example.domain.usecase.location

import com.example.domain.repository.location.CityNameResolver
import javax.inject.Inject

class GetCityNameUseCase @Inject constructor(
    private val resolver: CityNameResolver
) {
    suspend operator fun invoke(lat: Double, lon: Double): String? {
        return resolver.getCityName(lat, lon)
    }
}

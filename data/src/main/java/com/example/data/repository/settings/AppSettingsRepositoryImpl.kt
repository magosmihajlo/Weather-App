package com.example.data.repository.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.model.AppSettings
import com.example.domain.model.PressureUnit
import com.example.domain.model.TemperatureUnit
import com.example.domain.model.ThemeMode
import com.example.domain.model.TimeFormat
import com.example.domain.model.WindSpeedUnit
import com.example.domain.repository.AppSettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

@Singleton
class AppSettingsRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) : AppSettingsRepository {

    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val temperatureUnit = stringPreferencesKey("temperature_unit")
        val pressureUnit = stringPreferencesKey("pressure_unit")
        val windSpeedUnit = stringPreferencesKey("wind_speed_unit")
        val timeFormat = stringPreferencesKey("time_format")
        val locationEnabled = booleanPreferencesKey("location_enabled")
        val notificationsEnabled = booleanPreferencesKey("notifications_enabled")
        val themeMode = stringPreferencesKey("theme_mode")

    }

    override val appSettingsFlow: Flow<AppSettings> = dataStore.data
        .map { preferences ->
            AppSettings(
                temperatureUnit = TemperatureUnit.valueOf(preferences[PreferencesKeys.temperatureUnit] ?: TemperatureUnit.CELSIUS.name),
                pressureUnit = PressureUnit.valueOf(preferences[PreferencesKeys.pressureUnit] ?: PressureUnit.HPA.name),
                windSpeedUnit = WindSpeedUnit.valueOf(preferences[PreferencesKeys.windSpeedUnit] ?: WindSpeedUnit.KM_PER_HOUR.name),
                timeFormat = TimeFormat.valueOf(preferences[PreferencesKeys.timeFormat] ?: TimeFormat.TWENTY_FOUR_HOUR.name),
                locationEnabled = preferences[PreferencesKeys.locationEnabled] ?: false,
                notificationsEnabled = preferences[PreferencesKeys.notificationsEnabled] ?: false,
                themeMode = ThemeMode.valueOf(preferences[PreferencesKeys.themeMode] ?: ThemeMode.SYSTEM.name)
                )
        }

    override suspend fun updateTemperatureUnit(unit: TemperatureUnit) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.temperatureUnit] = unit.name
        }
    }

    override suspend fun updatePressureUnit(unit: PressureUnit) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.pressureUnit] = unit.name
        }
    }

    override suspend fun updateWindSpeedUnit(unit: WindSpeedUnit) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.windSpeedUnit] = unit.name
        }
    }

    override suspend fun updateTimeFormat(format: TimeFormat) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.timeFormat] = format.name
        }
    }

    override suspend fun updateLocationEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.locationEnabled] = enabled
        }
    }

    override suspend fun updateNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.notificationsEnabled] = enabled
        }
    }

    override suspend fun updateThemeMode(mode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.themeMode] = mode.name
        }
    }

}
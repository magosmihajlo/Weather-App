package com.example.presentation.navigation

sealed class AppScreen(val route: String) {
    object MainScreen : AppScreen("main_screen")
    object DetailsScreen : AppScreen("details_screen")
    object CitiesScreen : AppScreen("cities_screen")
    object SettingsScreen : AppScreen("settings_screen")
}
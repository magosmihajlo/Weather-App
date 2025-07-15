package com.example.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.presentation.screens.DetailsScreen
import com.example.presentation.screens.MainScreen
import com.example.presentation.screens.CitiesScreen
import com.example.presentation.screens.SettingsScreen
import com.example.presentation.viewmodel.ForecastViewModel
import com.example.presentation.viewmodel.SettingsViewModel
import com.example.presentation.viewmodel.WeatherViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    val weatherViewModel: WeatherViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val forecastViewModel: ForecastViewModel = hiltViewModel()


    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.MainScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.MainScreen.route) {
                MainScreen(
                    navController = navController,
                    weatherViewModel = weatherViewModel,
                    forecastViewModel = forecastViewModel
                )
            }
            composable(AppScreen.DetailsScreen.route) {
                DetailsScreen(navController = navController, viewModel = weatherViewModel
                )
            }
            composable(AppScreen.CitiesScreen.route) {
                CitiesScreen(navController = navController, weatherViewModel = weatherViewModel)
            }
            composable(AppScreen.SettingsScreen.route) {
                SettingsScreen(navController = navController, viewModel = settingsViewModel)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Details,
        NavigationItem.Cities,
        NavigationItem.Settings
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationRoute!!) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class NavigationItem(var route: String, var icon: ImageVector, var title: String) {
    object Home : NavigationItem(AppScreen.MainScreen.route, Icons.Filled.Home, "Home")
    object Details : NavigationItem(AppScreen.DetailsScreen.route, Icons.Filled.Info, "Details")
    object Cities : NavigationItem(AppScreen.CitiesScreen.route, Icons.Filled.LocationOn, "Cities")
    object Settings : NavigationItem(AppScreen.SettingsScreen.route, Icons.Filled.Settings, "Settings")
}

fun NavController.navigateWithState(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateWithState.graph.startDestinationRoute!!) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
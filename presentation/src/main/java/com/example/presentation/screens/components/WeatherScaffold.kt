package com.example.presentation.screens.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.presentation.navigation.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScaffold(
    navController: NavController,
    title: String,
    showActions: Boolean = true,
    onBack: (() -> Unit)? = null,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (showActions) {
                        IconButton(onClick = {
                            navController.navigate(AppScreen.CitiesScreen.route)
                        }) {
                            Icon(Icons.Filled.Search, contentDescription = "Recent Cities")
                        }
                        IconButton(onClick = {
                            navController.navigate(AppScreen.SettingsScreen.route)
                        }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Settings")
                        }
                    }
                }
            )
        },
        content = content
    )
}
package com.example.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.presentation.screens.components.WeatherContentStateView
import com.example.presentation.screens.components.WeatherScaffold
import com.example.presentation.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WeatherScaffold(
        navController = navController,
        title = "Detailed Weather",
        onBack = { navController.popBackStack() },
        showActions = false
    ) { padding ->
        WeatherContentStateView(
            uiState = uiState,
            modifier = Modifier.padding(padding),
            showMainInfo = true,
            showCurrentDetails = true,
            showFullDetails = true
        )
    }
}
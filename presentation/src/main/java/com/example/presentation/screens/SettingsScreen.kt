package com.example.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.domain.model.PressureUnit
import com.example.domain.model.TimeFormat
import com.example.domain.model.WindSpeedUnit
import com.example.presentation.screens.components.WeatherScaffold
import com.example.presentation.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val appSettings by viewModel.appSettings.collectAsStateWithLifecycle()

    WeatherScaffold(
        navController = navController,
        title = "Settings",
        showActions = false
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsSection(title = "MEASUREMENTS") {
                UnitSelectionRow(
                    label = "WIND SPEED",
                    options = WindSpeedUnit.entries.toTypedArray(),
                    selectedOption = appSettings.windSpeedUnit,
                    onOptionSelected = { viewModel.updateWindSpeedUnit(it) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                UnitSelectionRow(
                    label = "PRESSURE",
                    options = PressureUnit.entries.toTypedArray(),
                    selectedOption = appSettings.pressureUnit,
                    onOptionSelected = { viewModel.updatePressureUnit(it) }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            SettingsSection(title = "Notifications") {
                SettingsToggleItem(
                    label = "Notifications",
                    description = "Be aware of the weather",
                    checked = appSettings.notificationsEnabled,
                    onCheckedChange = { viewModel.updateNotificationsEnabled(it) }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            SettingsSection(title = "General") {
                SettingsToggleItem(
                    label = "12-Hour Time",
                    checked = appSettings.timeFormat == TimeFormat.TWELVE_HOUR,
                    onCheckedChange = {
                        viewModel.updateTimeFormat(if (it) TimeFormat.TWELVE_HOUR else TimeFormat.TWENTY_FOUR_HOUR)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                SettingsToggleItem(
                    label = "Location",
                    description = "Get weather of your location",
                    checked = appSettings.locationEnabled,
                    onCheckedChange = { viewModel.updateLocationEnabled(it) }
                )
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
fun <T : Enum<T>> UnitSelectionRow(
    label: String,
    options: Array<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                OutlinedButton(
                    onClick = { onOptionSelected(option) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent,
                        contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = (option as? PressureUnit)?.label ?: (option as? WindSpeedUnit)?.label ?: option.name)
                }
            }
        }
    }
}

@Composable
fun SettingsToggleItem(
    label: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = checked,
                onClick = { onCheckedChange(!checked) },
                role = Role.Switch
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            description?.let {
                Text(text = it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = null
        )
    }
}
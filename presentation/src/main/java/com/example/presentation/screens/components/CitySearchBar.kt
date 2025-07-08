package com.example.presentation.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun CitySearchBar(
    onSearch: (String) -> Unit
) {
    var cityInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = cityInput,
            onValueChange = { cityInput = it },
            label = { Text("Enter City") },
            singleLine = true,
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch(cityInput)
                keyboardController?.hide()
            })
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = {
            onSearch(cityInput)
            keyboardController?.hide()
        }) {
            Icon(Icons.Filled.Search, contentDescription = "Search city")
        }
    }
}

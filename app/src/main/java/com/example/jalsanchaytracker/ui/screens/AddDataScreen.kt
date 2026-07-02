package com.example.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jalsanchaytracker.viewmodel.RainfallViewModel

@Composable
fun AddDataScreen(viewModel: RainfallViewModel, onDataAdded: () -> Unit) {
    var rainfallMm by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Add Rainfall Data", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = rainfallMm,
            onValueChange = { rainfallMm = it; errorMessage = null },
            label = { Text("Rainfall (mm)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage != null
        )

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val mm = rainfallMm.toDoubleOrNull()
                if (mm == null || mm <= 0) {
                    errorMessage = "Enter a positive rainfall value."
                    return@Button
                }
                viewModel.addRainfallData(mm)
                onDataAdded()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate & Save")
        }
    }
}

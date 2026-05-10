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

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Add Rainfall Data", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = rainfallMm,
            onValueChange = { rainfallMm = it },
            label = { Text("Rainfall (mm)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val mm = rainfallMm.toDoubleOrNull() ?: 0.0
                if (mm > 0) {
                    viewModel.addRainfallData(mm)
                    onDataAdded()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate & Save")
        }
    }
}

package com.example.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jalsanchaytracker.viewmodel.RainfallViewModel
import com.example.jalsanchaytracker.viewmodel.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddDataScreen(viewModel: RainfallViewModel, onDataAdded: () -> Unit) {
    var rainfallMm by remember { mutableStateOf("") }
    var inputError by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { event ->
            when (event) {
                is UiEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                is UiEvent.ShowSuccess -> {
                    snackbarHostState.showSnackbar(event.message)
                    onDataAdded()
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(text = "Add Rainfall Data", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = rainfallMm,
                onValueChange = {
                    rainfallMm = it
                    inputError = null
                },
                label = { Text("Rainfall (mm)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = inputError != null,
                supportingText = inputError?.let { { Text(it) } }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val mm = rainfallMm.toDoubleOrNull()
                    when {
                        rainfallMm.isBlank() -> inputError = "Please enter a rainfall value"
                        mm == null -> inputError = "Please enter a valid number"
                        mm <= 0 -> inputError = "Rainfall must be greater than zero"
                        else -> {
                            inputError = null
                            viewModel.addRainfallData(mm)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calculate & Save")
            }
        }
    }
}

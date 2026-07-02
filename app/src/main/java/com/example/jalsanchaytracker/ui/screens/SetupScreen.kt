package com.example.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jalsanchaytracker.viewmodel.RainfallViewModel
import com.example.jalsanchaytracker.viewmodel.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SetupScreen(viewModel: RainfallViewModel, onComplete: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var roofArea by remember { mutableStateOf("") }
    var tankCapacity by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var roofAreaError by remember { mutableStateOf<String?>(null) }
    var tankCapacityError by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    LaunchedEffect(userProfile) {
        userProfile?.let {
            name = it.name
            roofArea = it.roofArea.toString()
            tankCapacity = it.tankCapacity.toString()
            location = it.location
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { event ->
            when (event) {
                is UiEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                is UiEvent.ShowSuccess -> {
                    snackbarHostState.showSnackbar(event.message)
                    onComplete()
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
            Text(text = "Profile Setup", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError != null,
                supportingText = nameError?.let { { Text(it) } }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = roofArea,
                onValueChange = {
                    roofArea = it
                    roofAreaError = null
                },
                label = { Text("Roof Area (sq ft)") },
                modifier = Modifier.fillMaxWidth(),
                isError = roofAreaError != null,
                supportingText = roofAreaError?.let { { Text(it) } }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = tankCapacity,
                onValueChange = {
                    tankCapacity = it
                    tankCapacityError = null
                },
                label = { Text("Tank Capacity (Liters)") },
                modifier = Modifier.fillMaxWidth(),
                isError = tankCapacityError != null,
                supportingText = tankCapacityError?.let { { Text(it) } }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    var hasError = false

                    if (name.isBlank()) {
                        nameError = "Name is required"
                        hasError = true
                    }

                    val roofAreaValue = roofArea.toDoubleOrNull()
                    if (roofArea.isBlank()) {
                        roofAreaError = "Roof area is required"
                        hasError = true
                    } else if (roofAreaValue == null) {
                        roofAreaError = "Please enter a valid number"
                        hasError = true
                    } else if (roofAreaValue <= 0) {
                        roofAreaError = "Roof area must be greater than zero"
                        hasError = true
                    }

                    val tankCapacityValue = tankCapacity.toDoubleOrNull()
                    if (tankCapacity.isBlank()) {
                        tankCapacityError = "Tank capacity is required"
                        hasError = true
                    } else if (tankCapacityValue == null) {
                        tankCapacityError = "Please enter a valid number"
                        hasError = true
                    } else if (tankCapacityValue <= 0) {
                        tankCapacityError = "Tank capacity must be greater than zero"
                        hasError = true
                    }

                    if (!hasError) {
                        viewModel.saveUserProfile(
                            name,
                            roofAreaValue!!,
                            tankCapacityValue!!,
                            location
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Profile")
            }
        }
    }
}

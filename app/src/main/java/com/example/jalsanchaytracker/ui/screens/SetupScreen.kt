package com.example.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jalsanchaytracker.viewmodel.RainfallViewModel

@Composable
fun SetupScreen(viewModel: RainfallViewModel, onComplete: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var roofArea by remember { mutableStateOf("") }
    var tankCapacity by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    LaunchedEffect(userProfile) {
        userProfile?.let {
            name = it.name
            roofArea = it.roofArea.toString()
            tankCapacity = it.tankCapacity.toString()
            location = it.location
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Profile Setup", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = name, onValueChange = { name = it; errorMessage = null }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = roofArea, onValueChange = { roofArea = it; errorMessage = null }, label = { Text("Roof Area (sq ft)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = tankCapacity, onValueChange = { tankCapacity = it; errorMessage = null }, label = { Text("Tank Capacity (Liters)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = location, onValueChange = { location = it; errorMessage = null }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(32.dp))

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val trimmedName = name.trim()
                val parsedRoofArea = roofArea.toDoubleOrNull()
                val parsedTankCapacity = tankCapacity.toDoubleOrNull()
                val trimmedLocation = location.trim()
                when {
                    trimmedName.isEmpty() -> {
                        errorMessage = "Name is required."
                        return@Button
                    }
                    parsedRoofArea == null || parsedRoofArea <= 0 -> {
                        errorMessage = "Roof area must be a positive number."
                        return@Button
                    }
                    parsedTankCapacity == null || parsedTankCapacity <= 0 -> {
                        errorMessage = "Tank capacity must be a positive number."
                        return@Button
                    }
                    trimmedLocation.isEmpty() -> {
                        errorMessage = "Location is required."
                        return@Button
                    }
                }
                viewModel.saveUserProfile(
                    trimmedName,
                    parsedRoofArea,
                    parsedTankCapacity,
                    trimmedLocation
                )
                onComplete()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Profile")
        }
    }
}

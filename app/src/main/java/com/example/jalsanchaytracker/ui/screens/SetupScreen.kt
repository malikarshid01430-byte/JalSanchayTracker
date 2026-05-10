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

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = roofArea, onValueChange = { roofArea = it }, label = { Text("Roof Area (sq ft)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = tankCapacity, onValueChange = { tankCapacity = it }, label = { Text("Tank Capacity (Liters)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.saveUserProfile(
                    name,
                    roofArea.toDoubleOrNull() ?: 0.0,
                    tankCapacity.toDoubleOrNull() ?: 0.0,
                    location
                )
                onComplete()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Profile")
        }
    }
}

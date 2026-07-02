package com.example.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jalsanchaytracker.ui.components.AppTextField
import com.example.jalsanchaytracker.ui.components.ScreenScaffold
import com.example.jalsanchaytracker.util.toSafeDouble
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

    ScreenScaffold(title = "Profile Setup") {
        AppTextField(value = name, onValueChange = { name = it }, label = "Name")
        Spacer(modifier = Modifier.height(8.dp))
        AppTextField(value = roofArea, onValueChange = { roofArea = it }, label = "Roof Area (sq ft)")
        Spacer(modifier = Modifier.height(8.dp))
        AppTextField(value = tankCapacity, onValueChange = { tankCapacity = it }, label = "Tank Capacity (Liters)")
        Spacer(modifier = Modifier.height(8.dp))
        AppTextField(value = location, onValueChange = { location = it }, label = "Location")

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.saveUserProfile(
                    name,
                    roofArea.toSafeDouble(),
                    tankCapacity.toSafeDouble(),
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

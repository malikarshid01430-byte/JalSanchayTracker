package com.example.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jalsanchaytracker.ui.components.AppTextField
import com.example.jalsanchaytracker.ui.components.ScreenScaffold
import com.example.jalsanchaytracker.util.toSafeDouble
import com.example.jalsanchaytracker.viewmodel.RainfallViewModel

@Composable
fun AddDataScreen(viewModel: RainfallViewModel, onDataAdded: () -> Unit) {
    var rainfallMm by remember { mutableStateOf("") }

    ScreenScaffold(title = "Add Rainfall Data") {
        AppTextField(
            value = rainfallMm,
            onValueChange = { rainfallMm = it },
            label = "Rainfall (mm)",
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val mm = rainfallMm.toSafeDouble()
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

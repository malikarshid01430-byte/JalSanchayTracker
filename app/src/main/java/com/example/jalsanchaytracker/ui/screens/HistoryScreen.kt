package com.example.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jalsanchaytracker.data.RainfallEntity
import com.example.jalsanchaytracker.viewmodel.RainfallViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(viewModel: RainfallViewModel) {
    val history by viewModel.allRainfallData.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Rainfall History", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(history, key = { it.id }) { entry ->
                HistoryItem(entry)
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun HistoryItem(entry: RainfallEntity) {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dateString = sdf.format(Date(entry.date))

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = dateString, fontWeight = FontWeight.Bold)
            Text(text = "${entry.rainfallMm} mm rainfall", style = MaterialTheme.typography.bodySmall)
        }
        Text(
            text = "+${entry.waterSavedLiters.toInt()} L",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

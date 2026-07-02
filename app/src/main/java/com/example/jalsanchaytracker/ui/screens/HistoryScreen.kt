package com.example.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jalsanchaytracker.data.RainfallEntity
import com.example.jalsanchaytracker.ui.components.ScreenScaffold
import com.example.jalsanchaytracker.util.DateFormatter
import com.example.jalsanchaytracker.viewmodel.RainfallViewModel

@Composable
fun HistoryScreen(viewModel: RainfallViewModel) {
    val history by viewModel.allRainfallData.collectAsStateWithLifecycle()

    ScreenScaffold(title = "Rainfall History", titleBottomSpacing = 16.dp) {
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
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = DateFormatter.formatTimestamp(entry.date), fontWeight = FontWeight.Bold)
            Text(text = "${entry.rainfallMm} mm rainfall", style = MaterialTheme.typography.bodySmall)
        }
        Text(
            text = "+${entry.waterSavedLiters.toInt()} L",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

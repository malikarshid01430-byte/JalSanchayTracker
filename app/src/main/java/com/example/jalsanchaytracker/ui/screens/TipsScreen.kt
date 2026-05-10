package com.example.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TipsScreen() {
    val tips = listOf(
        "Keep your roof clean to ensure high water quality.",
        "Check gutters for debris before the rainy season.",
        "Use first-flush diverters to remove the initial contaminated water.",
        "Regularly clean the filtration system.",
        "Ensure the tank overflow is directed to a recharge well.",
        "Monitor your water usage to optimize savings."
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Conservation Tips", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tips) { tip ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(text = tip, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

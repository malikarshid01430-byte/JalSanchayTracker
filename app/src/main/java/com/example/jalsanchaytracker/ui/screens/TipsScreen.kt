package com.example.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jalsanchaytracker.ui.components.ScreenScaffold

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

    ScreenScaffold(title = "Conservation Tips", titleBottomSpacing = 16.dp) {
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

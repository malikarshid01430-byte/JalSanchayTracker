package com.example.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jalsanchaytracker.viewmodel.RainfallViewModel
import com.github.mikephil.charting.charts.LineChart

@Composable
fun ReportScreen(viewModel: RainfallViewModel) {
    val chartData by viewModel.chartData.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Rainfall Analysis", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        if (chartData != null) {
            AndroidView(
                factory = { context ->
                    LineChart(context).apply {
                        description.isEnabled = false
                        xAxis.setDrawGridLines(false)
                        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                        axisRight.isEnabled = false
                        legend.isEnabled = true
                        setScaleEnabled(false)
                    }
                },
                update = { chart ->
                    chart.data = chartData
                    chart.invalidate()
                },
                modifier = Modifier.fillMaxWidth().height(300.dp)
            )
        } else {
            Text("No data available for reports.")
        }
    }
}

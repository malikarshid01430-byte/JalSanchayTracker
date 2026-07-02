package com.example.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jalsanchaytracker.ui.components.ScreenScaffold
import com.example.jalsanchaytracker.viewmodel.RainfallViewModel
import com.github.mikephil.charting.charts.LineChart

@Composable
fun ReportScreen(viewModel: RainfallViewModel) {
    val chartData by viewModel.chartData.collectAsStateWithLifecycle()

    ScreenScaffold(title = "Rainfall Analysis") {
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

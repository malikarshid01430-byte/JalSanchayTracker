package com.example.jalsanchaytracker.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jalsanchaytracker.viewmodel.RainfallViewModel

@Composable
fun DashboardScreen(viewModel: RainfallViewModel) {
    val totalSaved by viewModel.totalWaterSaved.collectAsStateWithLifecycle()
    val totalUsed by viewModel.totalWaterUsed.collectAsStateWithLifecycle()
    val available by viewModel.availableWater.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    
    val tankCapacity = remember(userProfile) { userProfile?.tankCapacity ?: 1000.0 }
    val fillPercentage = remember(available, tankCapacity) {
        if (tankCapacity > 0) (available.coerceIn(0.0, tankCapacity) / tankCapacity).toFloat() else 0f
    }

    var showUsageDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showUsageDialog = true },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Log Usage")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                HeaderSection(userProfile?.name ?: "User")
            }

            item {
                ModernWaterTankCard(fillPercentage, available, tankCapacity)
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCardSmall(
                        title = "Saved",
                        value = "${totalSaved?.toInt() ?: 0}L",
                        icon = Icons.Default.WaterDrop,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.weight(1f)
                    )
                    StatCardSmall(
                        title = "Used",
                        value = "${totalUsed?.toInt() ?: 0}L",
                        icon = Icons.Default.Opacity,
                        color = Color(0xFFF44336),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                InstructionSection()
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }

    if (showUsageDialog) {
        UsageInputDialog(
            onDismiss = { showUsageDialog = false },
            onConfirm = { amount ->
                viewModel.addWaterUsage(amount, "General")
                showUsageDialog = false
            }
        )
    }
}

@Composable
fun HeaderSection(name: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Welcome back, $name",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Text(
            text = "Your Water Summary",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A237E)
        )
    }
}

@Composable
fun ModernWaterTankCard(fillPercentage: Float, available: Double, capacity: Double) {
    val animatedFill by animateFloatAsState(
        targetValue = fillPercentage,
        animationSpec = tween(1500),
        label = "TankFill"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(120.dp, 160.dp)) {
                    val width = size.width
                    val height = size.height
                    
                    // Background path (tank shape)
                    drawRect(
                        color = Color(0xFFE0E0E0),
                        style = Stroke(width = 2.dp.toPx())
                    )
                    
                    // Water
                    val waterHeight = height * animatedFill
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF4FC3F7), Color(0xFF0288D1))
                        ),
                        topLeft = Offset(0f, height - waterHeight),
                        size = Size(width, waterHeight)
                    )
                }
            }

            Column {
                Text(
                    text = "${(fillPercentage * 100).toInt()}% Full",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0288D1)
                )
                Text(
                    text = "Current Status",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${available.toInt()} / ${capacity.toInt()} Liters",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun StatCardSmall(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun InstructionSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF1976D2))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "How to use?", fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
            }
            Spacer(modifier = Modifier.height(12.dp))
            InstructionItem("1. Add Rainfall data when it rains to track saved water.")
            InstructionItem("2. Tap the '-' button below to log water usage.")
            InstructionItem("3. Check 'History' for detailed logs.")
            InstructionItem("4. View 'Reports' for visual analysis of your savings.")
        }
    }
}

@Composable
fun InstructionItem(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color(0xFF455A64),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun UsageInputDialog(onDismiss: () -> Unit, onConfirm: (Double) -> Unit) {
    var amount by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Water Usage") },
        text = {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount (Liters)") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(amount.toDoubleOrNull() ?: 0.0) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

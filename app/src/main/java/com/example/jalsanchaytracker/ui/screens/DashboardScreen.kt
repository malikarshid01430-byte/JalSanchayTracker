package com.example.jalsanchaytracker.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.jalsanchaytracker.ui.components.AppTextField
import com.example.jalsanchaytracker.ui.theme.AlertRed
import com.example.jalsanchaytracker.ui.theme.BackgroundGray
import com.example.jalsanchaytracker.ui.theme.DarkNavy
import com.example.jalsanchaytracker.ui.theme.InfoBlue
import com.example.jalsanchaytracker.ui.theme.InfoBlueBg
import com.example.jalsanchaytracker.ui.theme.TankOutline
import com.example.jalsanchaytracker.ui.theme.TextDarkGray
import com.example.jalsanchaytracker.ui.theme.WaterBlue
import com.example.jalsanchaytracker.ui.theme.WaterBlueDark
import com.example.jalsanchaytracker.ui.theme.WaterBlueLight
import com.example.jalsanchaytracker.util.toSafeDouble
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
                .background(BackgroundGray)
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
                        color = WaterBlue,
                        modifier = Modifier.weight(1f)
                    )
                    StatCardSmall(
                        title = "Used",
                        value = "${totalUsed?.toInt() ?: 0}L",
                        icon = Icons.Default.Opacity,
                        color = AlertRed,
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
            color = DarkNavy
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
                    
                    drawRect(
                        color = TankOutline,
                        style = Stroke(width = 2.dp.toPx())
                    )
                    
                    val waterHeight = height * animatedFill
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(WaterBlueLight, WaterBlueDark)
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
                    color = WaterBlueDark
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
        colors = CardDefaults.cardColors(containerColor = InfoBlueBg)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = InfoBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "How to use?", fontWeight = FontWeight.Bold, color = InfoBlue)
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
        color = TextDarkGray,
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
            AppTextField(
                value = amount,
                onValueChange = { amount = it },
                label = "Amount (Liters)"
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(amount.toSafeDouble()) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

package com.example.jalsanchaytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jalsanchaytracker.ui.Screen
import com.example.jalsanchaytracker.ui.theme.JalSanchayTrackerTheme
import com.example.jalsanchaytracker.ui.screens.*
import com.example.jalsanchaytracker.viewmodel.RainfallViewModel
import com.example.jalsanchaytracker.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            val app = application as JalSanchayApplication
            val viewModel: RainfallViewModel = viewModel(
                factory = ViewModelFactory(
                    app.rainfallRepository,
                    app.userRepository,
                    app.waterUsageRepository
                )
            )
            
            JalSanchayTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
                    if (userProfile == null) {
                        LoginScreen(viewModel = viewModel, onLoginSuccess = { /* Navigation handled by State */ })
                    } else {
                        MainApp(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun MainApp(viewModel: RainfallViewModel) {
    val navController = rememberNavController()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    
    // Use a fixed start destination to avoid NavHost recreation issues
    // and handle initial navigation in a LaunchedEffect if needed.
    // However, since userProfile starts null, "login" is a safe start.
    val startDestination = if (userProfile == null) Screen.Login.route else Screen.Dashboard.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (userProfile != null) {
                BottomBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(viewModel = viewModel, onLoginSuccess = {
                    navController.navigate(Screen.Setup.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Setup.route) {
                SetupScreen(viewModel, onComplete = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Setup.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen(viewModel)
            }
            composable(Screen.AddData.route) {
                AddDataScreen(viewModel, onDataAdded = {
                    navController.navigate(Screen.Dashboard.route)
                })
            }
            composable(Screen.History.route) {
                HistoryScreen(viewModel)
            }
            composable(Screen.Tips.route) {
                TipsScreen()
            }
            composable(Screen.Profile.route) {
                SetupScreen(viewModel, onComplete = {
                    navController.navigate(Screen.Dashboard.route)
                })
            }
            composable(Screen.Reports.route) {
                ReportScreen(viewModel)
            }
        }
    }
}

@Composable
fun BottomBar(navController: androidx.navigation.NavHostController) {
    val items = listOf(
        Triple(Screen.Dashboard, "Home", Icons.Default.Home),
        Triple(Screen.AddData, "Add", Icons.Default.Add),
        Triple(Screen.History, "History", Icons.Default.List),
        Triple(Screen.Tips, "Tips", Icons.Default.Info),
        Triple(Screen.Reports, "Reports", Icons.Default.Star),
        Triple(Screen.Profile, "Profile", Icons.Default.Person)
    )
    
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        
        items.forEach { item ->
            val screen = item.first
            val label = item.second
            val icon = item.third

            NavigationBarItem(
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

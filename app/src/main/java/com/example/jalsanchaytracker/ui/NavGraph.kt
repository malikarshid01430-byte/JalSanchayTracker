package com.example.jalsanchaytracker.ui

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Setup : Screen("setup")
    object Dashboard : Screen("dashboard")
    object AddData : Screen("add_data")
    object History : Screen("history")
    object Tips : Screen("tips")
    object Profile : Screen("profile")
    object Reports : Screen("reports")
}

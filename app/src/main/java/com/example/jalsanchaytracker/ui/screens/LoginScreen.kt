package com.example.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jalsanchaytracker.viewmodel.RainfallViewModel

@Composable
fun LoginScreen(viewModel: RainfallViewModel, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Jal Sanchay Tracker", fontSize = 28.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    isLoading = true
                    // Mock Login: Save a default profile to trigger navigation
                    viewModel.saveUserProfile(
                        name = email.substringBefore("@"),
                        roofArea = 1000.0,
                        tankCapacity = 5000.0,
                        location = "Default Location"
                    )
                    onLoginSuccess()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }
        
        TextButton(onClick = { /* Handle signup */ }) {
            Text("Don't have an account? Sign Up")
        }
    }
}

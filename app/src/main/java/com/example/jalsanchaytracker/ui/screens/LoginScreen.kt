package com.example.jalsanchaytracker.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jalsanchaytracker.viewmodel.RainfallViewModel

@Composable
fun LoginScreen(viewModel: RainfallViewModel, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Jal Sanchay Tracker", fontSize = 28.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = null },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    val trimmedEmail = email.trim()
                    val trimmedPassword = password.trim()
                    when {
                        trimmedEmail.isEmpty() || trimmedPassword.isEmpty() -> {
                            errorMessage = "Email and password are required."
                            return@Button
                        }
                        !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> {
                            errorMessage = "Please enter a valid email address."
                            return@Button
                        }
                        trimmedPassword.length < 6 -> {
                            errorMessage = "Password must be at least 6 characters."
                            return@Button
                        }
                    }
                    isLoading = true
                    try {
                        viewModel.saveUserProfile(
                            name = trimmedEmail.substringBefore("@"),
                            roofArea = 1000.0,
                            tankCapacity = 5000.0,
                            location = "Default Location"
                        )
                        onLoginSuccess()
                    } catch (e: Exception) {
                        isLoading = false
                        errorMessage = "Login failed. Please try again."
                    }
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

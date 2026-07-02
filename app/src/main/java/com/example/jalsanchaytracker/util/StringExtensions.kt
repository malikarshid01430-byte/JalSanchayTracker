package com.example.jalsanchaytracker.util

fun String.toSafeDouble(default: Double = 0.0): Double =
    toDoubleOrNull() ?: default

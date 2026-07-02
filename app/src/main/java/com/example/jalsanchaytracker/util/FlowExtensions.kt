package com.example.jalsanchaytracker.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

private const val STOP_TIMEOUT_MS = 5_000L

fun <T> Flow<T>.stateInWhileSubscribed(
    scope: CoroutineScope,
    initialValue: T
): StateFlow<T> = stateIn(
    scope,
    SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
    initialValue
)

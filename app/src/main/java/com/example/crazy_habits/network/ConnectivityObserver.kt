package com.example.crazy_habits.network

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<NetworkStatus>

    enum class NetworkStatus {
        Available,
        Unavailable,
        Losing,
        Lost
    }
}
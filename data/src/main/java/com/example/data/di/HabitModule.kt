package com.example.data.di

import com.example.data.HabitEditRepositoryImpl
import com.example.data.HabitListRepositoryImpl
import com.example.data.remote.network.ConnectivityObserver
import com.example.data.remote.network.NetworkConnectivityObserver
import com.example.domain.repository.HabitEditRepository
import com.example.domain.repository.HabitListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HabitModule {

    @Binds
    @Singleton
    abstract fun bindHabitRepository(impl: HabitListRepositoryImpl): HabitListRepository

    @Binds
    @Singleton
    abstract fun bindHabitEditRepository(impl: HabitEditRepositoryImpl): HabitEditRepository

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(impl: NetworkConnectivityObserver): ConnectivityObserver

}
package com.example.domain.di

import com.example.domain.entities.Type
import com.example.domain.repository.HabitListRepository
import com.example.domain.usecase.SortFilterHabitsUseCase
import com.example.domain.usecase.SyncHabitsWithServerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {


    @Qualifier
    annotation class GoodHabitSync

    @Qualifier
    annotation class BadHabitSync

    @Qualifier
    annotation class GoodHabitFilter

    @Qualifier
    annotation class BadHabitFilter

    @Provides
    @GoodHabitSync
    @Singleton
    fun provideSyncHabitsWithServerUseCaseGood(habitListRepository: HabitListRepository): SyncHabitsWithServerUseCase {
        return SyncHabitsWithServerUseCase(habitListRepository, Type.Good)
    }

    @Provides
    @BadHabitSync
    @Singleton
    fun provideSyncHabitsWithServerUseCaseBad(habitListRepository: HabitListRepository): SyncHabitsWithServerUseCase {
        return SyncHabitsWithServerUseCase(habitListRepository, Type.Bad)
    }

    @Provides
    @GoodHabitFilter
    @Singleton
    fun provideSortFilterHabitsUseCaseGood(habitListRepository: HabitListRepository): SortFilterHabitsUseCase {
        return SortFilterHabitsUseCase(habitListRepository, Type.Good)
    }

    @Provides
    @BadHabitFilter
    @Singleton
    fun provideSortFilterHabitsUseCaseBad(habitListRepository: HabitListRepository): SortFilterHabitsUseCase {
        return SortFilterHabitsUseCase(habitListRepository, Type.Bad)
    }

}
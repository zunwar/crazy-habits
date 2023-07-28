package com.example.presentation.edithabit.di

import androidx.lifecycle.SavedStateHandle
import com.example.presentation.listhabits.ListHabitsFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object HabitEditModule {

    @Qualifier
    annotation class IdHabit

    @Qualifier
    annotation class IsEditable

    @Provides
    @IdHabit
    @ViewModelScoped
    fun provideIdHabit(savedStateHandle: SavedStateHandle): String? = savedStateHandle.get<String>(
        ListHabitsFragment.HABIT_TO_EDIT_ID
    )

    @Provides
    @IsEditable
    @ViewModelScoped
    fun provideIsEditable(@IdHabit provideIdHabit: String?): Boolean =
        provideIdHabit != null
}
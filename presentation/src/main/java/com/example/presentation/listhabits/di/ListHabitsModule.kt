package com.example.presentation.listhabits.di

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
object ListHabitsModule {

        @Qualifier
        annotation class IsBadInstance

        @Provides
        @IsBadInstance
        @ViewModelScoped
        fun provideIsBadInstance(savedStateHandle: SavedStateHandle): Boolean = savedStateHandle.get<Boolean>(
            ListHabitsFragment.BAD_INSTANCE) == true

}
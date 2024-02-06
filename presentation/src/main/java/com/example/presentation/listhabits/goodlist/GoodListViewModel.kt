package com.example.presentation.listhabits.goodlist

import android.net.Uri
import androidx.lifecycle.*
import com.example.domain.usecase.*
import com.example.presentation.listhabits.BaseListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import com.example.domain.di.DomainModule

@HiltViewModel
class GoodListViewModel @Inject constructor(
    @DomainModule.GoodHabitFilter sortFilterHabitsUseCase: SortFilterHabitsUseCase,
    @DomainModule.GoodHabitSync syncHabitsWithServerUseCase: SyncHabitsWithServerUseCase,
    deleteHabitUseCase: DeleteHabitUseCase,
    private val getAvatarUriUseCase: GetAvatarUriUseCase,
    doHabitUseCase: DoHabitUseCase,
    nextSortStateUseCase: NextSortStateUseCase,
) : BaseListViewModel(
    syncHabitsWithServerUseCase,
    sortFilterHabitsUseCase,
    deleteHabitUseCase,
    doHabitUseCase,
    nextSortStateUseCase,
) {

    private val _uri: MutableLiveData<Uri> = MutableLiveData()
    val uri: LiveData<Uri> = _uri

    init {
        getUriToAvatarDownload()
    }

    private fun getUriToAvatarDownload() {
        viewModelScope.launch {
            getAvatarUriUseCase().collect { _uri.postValue(it) }
        }
    }

}
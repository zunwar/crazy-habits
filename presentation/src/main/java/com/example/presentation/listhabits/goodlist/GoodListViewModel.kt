package com.example.presentation.listhabits.goodlist

import android.net.Uri
import androidx.lifecycle.*
import com.example.domain.usecase.*
import com.example.presentation.listhabits.BaseListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class GoodListViewModel @Inject constructor(
    sortFilterHabitsUseCase: SortFilterHabitsUseCase,
    deleteHabitUseCase: DeleteHabitUseCase,
    private val getAvatarUriUseCase: GetAvatarUriUseCase,
    syncHabitsWithServerUseCase: SyncHabitsWithServerUseCase,
    doHabitUseCase: DoHabitUseCase,
) : BaseListViewModel(
    sortFilterHabitsUseCase,
    deleteHabitUseCase,
    syncHabitsWithServerUseCase,
    doHabitUseCase,
    isBadList = false
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
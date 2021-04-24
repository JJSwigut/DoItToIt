package com.jjswigut.feature.lists

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.jjswigut.core.base.BaseViewModel
import com.jjswigut.core.utils.State
import com.jjswigut.data.local.SwipeEvent
import com.jjswigut.data.local.entities.ListEntity
import com.jjswigut.data.local.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListsViewModel @Inject constructor(
    private val repo: Repository
) : BaseViewModel() {

    private val listsEventChannel = Channel<SwipeEvent>()
    val listsEvent = listsEventChannel.receiveAsFlow()

    val allLists = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            repo.allLists.collect { lists ->
                emit(State.Success(lists))
            }
        } catch (exception: Exception) {
            emit(State.Failed(exception.toString()))
            Log.d(TAG, ":${exception.message} ")
        }
    }

    fun deleteList(listId: Long) = viewModelScope.launch { repo.deleteList(listId) }

    fun onListSwiped(list: ListEntity) = viewModelScope.launch {

//        repo.deleteList(list.listId)
        listsEventChannel.send(SwipeEvent.ShowUndoDeleteListMessage(list))
    }

    fun onUndoDeleteClick(list: ListEntity) = viewModelScope.launch {
        repo.addList(list)
    }
}
package com.jjswigut.feature.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.liveData
import com.jjswigut.core.base.BaseViewModel
import com.jjswigut.core.utils.State
import com.jjswigut.data.local.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: Repository
) : BaseViewModel() {

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
}
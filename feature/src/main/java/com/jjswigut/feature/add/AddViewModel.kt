package com.jjswigut.feature.add

import androidx.lifecycle.viewModelScope
import com.jjswigut.core.base.BaseViewModel
import com.jjswigut.data.local.entities.ListEntity
import com.jjswigut.data.local.entities.TaskEntity
import com.jjswigut.data.local.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repo: Repository
) : BaseViewModel() {

    fun addList(list: ListEntity) = viewModelScope.launch { repo.addList(list) }

    fun addTask(task: TaskEntity) = viewModelScope.launch { repo.addTask(task) }
}
package com.jjswigut.data.local

import com.jjswigut.data.local.entities.ListEntity
import com.jjswigut.data.local.entities.TaskEntity

sealed class SwipeEvent {
    data class DeleteTask(val task: TaskEntity, val position: Int) : SwipeEvent()
    data class DeleteList(val list: ListEntity, val position: Int) : SwipeEvent()
}

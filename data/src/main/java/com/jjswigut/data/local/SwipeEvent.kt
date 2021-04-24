package com.jjswigut.data.local

import com.jjswigut.data.local.entities.ListEntity
import com.jjswigut.data.local.entities.TaskEntity

sealed class SwipeEvent {
    data class ShowUndoDeleteTaskMessage(val task: TaskEntity) : SwipeEvent()
    data class ShowUndoDeleteListMessage(val list: ListEntity, val position: Int) : SwipeEvent()
}

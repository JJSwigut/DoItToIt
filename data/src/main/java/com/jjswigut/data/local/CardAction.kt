package com.jjswigut.data.local

import com.jjswigut.data.local.entities.ListEntity
import com.jjswigut.data.local.entities.TaskEntity

sealed class CardAction {
    data class ListCardClicked(val list: ListEntity) : CardAction()
    data class TaskCardClicked(val task: TaskEntity) : CardAction()
    data class AddCardClicked(val type: AddType) : CardAction()
}
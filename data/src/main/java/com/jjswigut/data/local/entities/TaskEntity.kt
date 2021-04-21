package com.jjswigut.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey
    val taskId: Long,
    val parentListId: Long,
    val body: String
) {
}
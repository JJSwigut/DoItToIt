package com.jjswigut.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ListEntity::class,
            parentColumns = ["listId"],
            childColumns = ["parentListId"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val taskId: Long = 0,
    val parentListId: Long,
    val body: String
)
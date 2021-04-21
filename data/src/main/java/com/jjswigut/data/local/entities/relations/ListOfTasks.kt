package com.jjswigut.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.jjswigut.data.local.entities.ListEntity
import com.jjswigut.data.local.entities.TaskEntity

data class ListOfTasks(
    @Embedded val list: ListEntity,
    @Relation(
        parentColumn = "listId",
        entityColumn = "parentListId",


        )
    var tasks: List<TaskEntity>
)

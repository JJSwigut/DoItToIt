package com.jjswigut.data.local.repositories

import com.jjswigut.data.local.dao.Dao
import com.jjswigut.data.local.entities.ListEntity
import com.jjswigut.data.local.entities.TaskEntity
import com.jjswigut.data.local.entities.relations.ListOfTasks
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val dao: Dao
) {

    val allLists: Flow<List<ListEntity>> get() = dao.getAllLists()

    fun getListOfTasks(listId: Long): Flow<List<ListOfTasks>> {
        return dao.getListofTasks(listId)
    }

    suspend fun addList(list: ListEntity) = dao.insertList(list)

    suspend fun addTask(task: TaskEntity) = dao.insertTask(task)

    suspend fun updateTask(task: TaskEntity) = dao.updateTask(task)

    suspend fun deleteList(listId: Long) = dao.deleteList(listId)

    suspend fun deleteTask(task: TaskEntity) = dao.deleteTask(task)
}

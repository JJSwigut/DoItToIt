package com.jjswigut.data.local.dao

import androidx.room.*
import androidx.room.Dao
import com.jjswigut.data.local.entities.ListEntity
import com.jjswigut.data.local.entities.TaskEntity
import com.jjswigut.data.local.entities.relations.ListOfTasks
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(task: ListEntity)

    @Query("SELECT * FROM list_table")
    fun getAllLists(): Flow<List<ListEntity>>

    @Transaction
    @Query("SELECT * FROM list_table")
    fun getListofTasks(): Flow<List<ListOfTasks>>
}
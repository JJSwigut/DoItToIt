package com.jjswigut.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jjswigut.data.local.dao.Dao
import com.jjswigut.data.local.entities.ListEntity
import com.jjswigut.data.local.entities.TaskEntity

@Database(
    entities = [TaskEntity::class, ListEntity::class],
    version = 4,
    exportSchema = false
)

abstract class MainDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}
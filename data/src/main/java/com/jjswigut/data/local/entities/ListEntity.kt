package com.jjswigut.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_table")
data class ListEntity(
    @PrimaryKey(autoGenerate = true)
    val listId: Long = 0,
    @ColumnInfo
    val name: String
) {

}
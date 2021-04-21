package com.jjswigut.data.local.repositories

import com.jjswigut.data.local.dao.Dao
import com.jjswigut.data.local.entities.ListEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val dao: Dao
) {

    val allLists: Flow<List<ListEntity>> get() = dao.getAllLists()


}


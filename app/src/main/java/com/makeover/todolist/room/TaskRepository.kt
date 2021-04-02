package com.makeover.todolist.room

import com.makeover.todolist.room.dao.CategoryDao
import com.makeover.todolist.room.model.Category
import javax.inject.Inject

class TaskRepository @Inject constructor(private val categoryDao: CategoryDao) {

    suspend fun getCategoryList() = categoryDao.getCategoryList()

    suspend fun insertCategory(category : Category) = categoryDao.insertCategory(category)

}
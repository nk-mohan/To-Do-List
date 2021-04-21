package com.makeover.todolist.room

import com.makeover.todolist.room.dao.CategoryDao
import com.makeover.todolist.room.model.Category
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryDao: CategoryDao) {

    suspend fun getCategory(categoryId:Int) = categoryDao.getCategory(categoryId)

    suspend fun getCategoryList() = categoryDao.getCategoryList()

    suspend fun insertCategory(category : Category) = categoryDao.insertCategory(category)

    suspend fun updateCategory(categoryName : String, categoryId:Int) = categoryDao.updateCategory(categoryName, categoryId)

    suspend fun deleteCategory(categoryId:Int) = categoryDao.deleteCategory(categoryId)

}
package com.makeover.todolist.room.dao

import androidx.room.*
import com.makeover.todolist.room.model.Category

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(vararg category: Category)

    @Query("UPDATE CATEGORY SET name = :categoryName WHERE id =:categoryId")
    suspend fun updateCategory(categoryName: String, categoryId: Int)

    @Query("SELECT * FROM Category")
    suspend fun getCategoryList(): List<Category>

    @Query("SELECT * FROM Category WHERE id = :categoryId")
    fun getCategory(categoryId: Int): Category

    @Query("DELETE FROM CATEGORY WHERE id =:categoryId")
    suspend fun deleteCategory(categoryId: Int)

}
package com.makeover.todolist.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.makeover.todolist.room.model.Category

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(vararg category: Category)

    @Query("SELECT * FROM Category")
    suspend fun getCategoryList(): List<Category>

}
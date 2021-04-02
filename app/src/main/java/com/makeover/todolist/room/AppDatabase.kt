package com.makeover.todolist.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.makeover.todolist.room.dao.CategoryDao
import com.makeover.todolist.room.model.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskCategoryDao(): CategoryDao
}
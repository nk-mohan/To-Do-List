package com.makeover.todolist.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.makeover.todolist.room.dao.CategoryDao
import com.makeover.todolist.room.dao.TaskDao
import com.makeover.todolist.room.model.Category
import com.makeover.todolist.room.model.Task

@Database(entities = [Category::class, Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskCategoryDao(): CategoryDao
    abstract fun taskDao(): TaskDao
}
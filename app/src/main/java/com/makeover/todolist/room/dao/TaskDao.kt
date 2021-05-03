package com.makeover.todolist.room.dao

import androidx.room.*
import com.makeover.todolist.room.model.Task

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Query("UPDATE TASK SET title =:title, description =:description, date=:date, hour=:hour, minute=:minute  WHERE id =:id")
    suspend fun updateTask(
        id: Int,
        title: String,
        description: String,
        date: Long?,
        hour: Int?,
        minute: Int?
    )

    @Query("UPDATE TASK SET date=:date, hour=:hour, minute=:minute  WHERE id =:id")
    suspend fun updateTaskTime(
        id: Int,
        date: Long?,
        hour: Int?,
        minute: Int?
    )

    @Query("SELECT * FROM TASK")
    suspend fun getTaskList(): List<Task>

    @Query("SELECT * FROM TASK WHERE id = :taskId")
    suspend fun getTask(taskId: Int): Task

    @Query("SELECT * FROM TASK WHERE category_id = :categoryId")
    suspend fun getTaskListById(categoryId: Int): List<Task>

    @Query("DELETE FROM TASK WHERE id = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("DELETE FROM TASK WHERE category_id = :categoryId")
    suspend fun deleteAllTaskByCategory(categoryId: Int)
}
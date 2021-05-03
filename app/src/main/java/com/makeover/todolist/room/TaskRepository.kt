package com.makeover.todolist.room

import com.makeover.todolist.room.dao.TaskDao
import com.makeover.todolist.room.model.Task
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    suspend fun getTaskList() = taskDao.getTaskList()

    suspend fun getTaskListByCategory(categoryId: Int) = taskDao.getTaskListById(categoryId)

    suspend fun getTask(taskId: Int) = taskDao.getTask(taskId)

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    suspend fun updateTask(
        id: Int,
        title: String,
        description: String,
        date: Long?,
        hour: Int?,
        minute: Int?
    ) = taskDao.updateTask(id, title, description, date, hour, minute)

    suspend fun updateTaskTime(
        id: Int,
        date: Long?,
        hour: Int?,
        minute: Int?
    ) = taskDao.updateTaskTime(id, date, hour, minute)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(taskId: Int) = taskDao.deleteTask(taskId)

    suspend fun deleteAllTaskByCategory(categoryId: Int) = taskDao.deleteAllTaskByCategory(categoryId)
}
package com.makeover.todolist.viewmodel

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import com.makeover.todolist.helper.CategoryDiffCallback
import com.makeover.todolist.helper.TaskDiffCallback
import com.makeover.todolist.room.CategoryRepository
import com.makeover.todolist.room.TaskRepository
import com.makeover.todolist.room.model.Category
import com.makeover.todolist.room.model.Task
import com.makeover.todolist.scheduler.ScheduleTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel @ViewModelInject constructor(
    private val categoryRepository: CategoryRepository,
    private val taskRepository: TaskRepository,
    private val scheduleTask: ScheduleTask
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    var categoryId: Int = 0
    var taskId: Int = 0

    val category: MutableLiveData<Category> = MutableLiveData()
    var categoryList = mutableListOf<Category>()
    var categoryListAdapter = mutableListOf<Category>()
    val categoryDiffResult = MutableLiveData<DiffUtil.DiffResult>()

    val task: MutableLiveData<Task> = MutableLiveData()
    val taskList: MutableLiveData<List<Task>> = MutableLiveData()
    val taskListByCategory = mutableListOf<Task>()
    val taskListByCategoryAdapter = mutableListOf<Task>()
    val taskDiffResult = MutableLiveData<DiffUtil.DiffResult>()

    /* Category Related Action from Below */

    fun createCategory(categoryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.insertCategory(Category(categoryName))
            getCategoryList()
        }
    }

    fun updateCategory(categoryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.updateCategory(categoryName, categoryId)
            getCategory(categoryId)
            getCategoryList()
        }
    }

    fun getCategoryList() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryList.clear()
            categoryList.addAll(categoryRepository.getCategoryList())
            getCategoryDiffResult()
        }
    }

    private fun getCategoryDiffResult() {
        viewModelScope.launch(Dispatchers.IO) {
            val diffResult =
                getDiffUtilResult(CategoryDiffCallback(categoryListAdapter, categoryList))
            categoryListAdapter.clear()
            categoryListAdapter.addAll(categoryList)
            categoryDiffResult.postValue(diffResult)
        }
    }

    private suspend fun getDiffUtilResult(diffUtilCallback: DiffUtil.Callback): DiffUtil.DiffResult =
        withContext(Dispatchers.IO) {
            DiffUtil.calculateDiff(diffUtilCallback)
        }

    fun getCategory(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            category.postValue(categoryRepository.getCategory(categoryId))
        }
    }

    fun deleteCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.deleteCategory(categoryId)
            taskRepository.deleteAllTaskByCategory(categoryId)
        }
    }


    /* Task Related Action from Below */

    fun getTask(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            task.postValue(taskRepository.getTask(taskId))
        }
    }

    fun getTaskList() {
        viewModelScope.launch(Dispatchers.IO) {
            taskList.postValue(taskRepository.getTaskList())
        }
    }

    fun getTaskListById(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            taskListByCategory.clear()
            taskListByCategory.addAll(taskRepository.getTaskListByCategory(categoryId))
            getTaskDiffResult()
        }
    }

    private fun getTaskDiffResult() {
        viewModelScope.launch(Dispatchers.IO) {
            val diffResult =
                getDiffUtilResult(TaskDiffCallback(taskListByCategoryAdapter, taskListByCategory))
            taskListByCategoryAdapter.clear()
            taskListByCategoryAdapter.addAll(taskListByCategory)
            taskDiffResult.postValue(diffResult)
        }
    }

    fun createTask(
        taskTitle: String,
        taskDescription: String,
        date: Long?,
        hour: Int?,
        minute: Int?,
        context: Context?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = Task(categoryId, taskTitle, taskDescription, date, hour, minute)
            val taskId = taskRepository.insertTask(task)
            getTaskListById(categoryId)
            scheduleTask.scheduleTask(taskId, context)
        }
    }

    fun updateTask(taskTitle: String, taskDescription: String) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(taskId, taskTitle, taskDescription)
            getTaskListById(categoryId)
            getTask(taskId)
        }
    }

    fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.deleteTask(taskId)
        }
    }
}
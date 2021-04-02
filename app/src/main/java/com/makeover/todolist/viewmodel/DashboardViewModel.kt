package com.makeover.todolist.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makeover.todolist.room.TaskRepository
import com.makeover.todolist.room.model.Category
import kotlinx.coroutines.launch

class DashboardViewModel @ViewModelInject constructor(private val taskRepository: TaskRepository): ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    var categoryList = MutableLiveData<List<Category>>()

    fun createCategory(categoryName: String){
        viewModelScope.launch {
            taskRepository.insertCategory(Category(categoryName))
        }
    }

    fun getCategoryList(){
        viewModelScope.launch {
            categoryList.postValue(taskRepository.getCategoryList())
        }
    }
}
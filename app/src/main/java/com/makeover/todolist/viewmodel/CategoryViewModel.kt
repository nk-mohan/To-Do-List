package com.makeover.todolist.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import com.makeover.todolist.helper.CategoryDiffCallback
import com.makeover.todolist.room.TaskRepository
import com.makeover.todolist.room.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryViewModel @ViewModelInject constructor(private val taskRepository: TaskRepository) :
    ViewModel() {

    var categoryList = mutableListOf<Category>()
    var categoryListAdapter = mutableListOf<Category>()

    val categoryDiffResult = MutableLiveData<DiffUtil.DiffResult>()

    fun getCategoryList() {
        viewModelScope.launch {
            categoryList.clear()
            categoryList.addAll(taskRepository.getCategoryList())
            getCategoryDiffResult()
        }
    }

    private fun getCategoryDiffResult() {
        viewModelScope.launch {
            val diffResult = getDiffUtilResult(CategoryDiffCallback(categoryListAdapter, categoryList))
            categoryListAdapter.clear()
            categoryListAdapter.addAll(categoryList)
            categoryDiffResult.postValue(diffResult)
        }
    }

    private suspend fun getDiffUtilResult(diffUtilCallback: DiffUtil.Callback): DiffUtil.DiffResult = withContext(Dispatchers.IO) {
        DiffUtil.calculateDiff(diffUtilCallback)
    }
}
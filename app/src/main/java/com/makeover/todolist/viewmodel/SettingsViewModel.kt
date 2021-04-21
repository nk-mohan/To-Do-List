package com.makeover.todolist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.makeover.todolist.utils.AppConstants
import com.makeover.todolist.utils.SharedPreferenceManager

class SettingsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is settings Fragment"
    }
    val text: LiveData<String> = _text

    val selectedTheme = MutableLiveData<Int>()

    fun getSelectedTheme() {
        selectedTheme.postValue(SharedPreferenceManager.getIntValue(AppConstants.DAY_NIGHT_MODE))
    }
}
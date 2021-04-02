package com.makeover.todolist.di

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.makeover.todolist.utils.AppUtils
import com.makeover.todolist.utils.SharedPreferenceManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferenceManager.init(this)
        AppCompatDelegate.setDefaultNightMode(AppUtils.getNightMode())
    }
}
package com.makeover.todolist.utils

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

object AppUtils {

    fun getNightMode(): Int {
        return when (SharedPreferenceManager.getIntValue(AppConstants.DAY_NIGHT_MODE, -1)) {
            AppConstants.THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
            AppConstants.THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }
        }
    }
}
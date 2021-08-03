package com.makeover.todolist.utils

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.makeover.todolist.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

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

    fun getTimeString(context: Context, hour: Int?, minute: Int?): String {
        hour?.let {
            return if (hour < 12)
                String.format(context.getString(R.string.time_format_am), hour, minute)
            else
                String.format(context.getString(R.string.time_format_pm), hour % 12, minute)
        }
        return AppConstants.EMPTY_STRING
    }

    fun getDateAndTimeString(context: Context, date: Long?, hour: Int?, minute: Int?): String {
        date?.let {
            val timeZoneDate = SimpleDateFormat("EEE dd MMM, yyyy, ", Locale.getDefault())
            return timeZoneDate.format(Date(date)) + getTimeString(context, hour, minute)
        }
        return AppConstants.EMPTY_STRING
    }

    private var density = 1f

    fun dp(value: Float, context: Context): Int {
        if (density == 1f) {
            checkDisplaySize(context)
        }
        return if (value == 0f) {
            0
        } else ceil((density * value).toDouble()).toInt()
    }


    private fun checkDisplaySize(context: Context) {
        try {
            density = context.resources.displayMetrics.density
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
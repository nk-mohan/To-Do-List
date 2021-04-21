package com.makeover.todolist.utils

import com.makeover.todolist.BuildConfig

object AppConstants {
    const val NOTIFICATION_ID = "${BuildConfig.APPLICATION_ID}_notification_id"
    const val NOTIFICATION_NAME = BuildConfig.APPLICATION_ID
    const val NOTIFICATION_CHANNEL = "${BuildConfig.APPLICATION_ID}_channel_01"
    const val NOTIFICATION_TITLE = "${BuildConfig.APPLICATION_ID}_title"
    const val NOTIFICATION_SUB_TITLE = "${BuildConfig.APPLICATION_ID}_sub_title"
    const val NOTIFICATION_WORK = "${BuildConfig.APPLICATION_ID}_notification_work"


    const val DAY_NIGHT_MODE = "day_night_mode"
    const val DAY_NIGHT_MODE_UPDATED = "day_night_mode_updated"


    const val THEME_LIGHT = 0
    const val THEME_DARK = 1
    const val THEME_SYSTEM_DEFAULT = 2

    const val CREATE_TASK = "create_task"
    const val IS_EDIT = "is_edit"
    const val TASK_ID = "task_id"

    const val EMPTY_STRING = ""
}
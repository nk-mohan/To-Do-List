package com.makeover.todolist.databinding

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.makeover.todolist.R
import com.makeover.todolist.`interface`.ChooseOneAlertDialogListener
import com.makeover.todolist.utils.AppUtils
import com.makeover.todolist.utils.AppConstants
import com.makeover.todolist.utils.SharedPreferenceManager
import com.makeover.todolist.view.customviews.AlertDialogView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SettingsDataBinding @Inject constructor(@ApplicationContext val context: Context) {

    @Inject
    lateinit var alertDialogView: AlertDialogView

    private var activity: Activity? = null

    fun setActivity(activity: Activity?) {
        this.activity = activity
    }

    fun changeTheme() {
        var choices: Array<String> = context.resources.getStringArray(R.array.choose_theme_choices)
        val selectedMode = SharedPreferenceManager.getIntValue(AppConstants.DAY_NIGHT_MODE)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            choices = choices.sliceArray(IntRange(0, 1))
        alertDialogView.showChooseOneAlertDialog(activity, context.resources.getString(R.string.choose_theme),
            choices, selectedMode,
            context.resources.getString(R.string.positive_button),
            context.resources.getString(R.string.negative_button),
            object : ChooseOneAlertDialogListener {
                override fun selectedItem(position: Int) {
                    SharedPreferenceManager.setIntValue(AppConstants.DAY_NIGHT_MODE, position)
                    AppCompatDelegate.setDefaultNightMode(AppUtils.getNightMode())
                }
            })
    }

    val selectedTheme
        get() = when (SharedPreferenceManager.getIntValue(AppConstants.DAY_NIGHT_MODE)) {
            AppConstants.THEME_DARK -> context.resources.getString(R.string.theme_dark)
            AppConstants.THEME_SYSTEM_DEFAULT -> context.resources.getString(R.string.theme_system_default)
            else -> context.resources.getString(R.string.theme_light)
        }
}
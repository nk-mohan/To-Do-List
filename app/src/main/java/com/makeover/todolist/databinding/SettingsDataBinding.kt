package com.makeover.todolist.databinding

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.makeover.todolist.R
import com.makeover.todolist.`interface`.AlertDialogListener
import com.makeover.todolist.utils.AppUtils
import com.makeover.todolist.utils.Constants
import com.makeover.todolist.utils.SharedPreferenceManager
import com.makeover.todolist.view.customviews.AlertDialog
import com.makeover.todolist.viewmodel.SettingsViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SettingsDataBinding @Inject constructor(@ApplicationContext val context: Context) {

    @Inject
    lateinit var alertDialog: AlertDialog

    private var activity: Activity? = null

    fun setActivity(activity: Activity?) {
        this.activity = activity
    }

    fun changeTheme() {
        var choices: Array<String> = context.resources.getStringArray(R.array.choose_theme_choices)
        val selectedMode = SharedPreferenceManager.getIntValue(Constants.DAY_NIGHT_MODE)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            choices = choices.sliceArray(IntRange(0, 1))
        alertDialog.showAlertDialog(activity, context.resources.getString(R.string.choose_theme),
            choices, selectedMode,
            context.resources.getString(R.string.positive_button),
            context.resources.getString(R.string.negative_button),
            object : AlertDialogListener {
                override fun selectedItem(position: Int) {
                    SharedPreferenceManager.setIntValue(Constants.DAY_NIGHT_MODE, position)
                    SharedPreferenceManager.setBooleanValue(Constants.DAY_NIGHT_MODE_UPDATED, true)
                    AppCompatDelegate.setDefaultNightMode(AppUtils.getNightMode())
                }
            })
    }

    val selectedTheme
        get() = when (SharedPreferenceManager.getIntValue(Constants.DAY_NIGHT_MODE)) {
            Constants.THEME_DARK -> context.resources.getString(R.string.theme_dark)
            Constants.THEME_SYSTEM_DEFAULT -> context.resources.getString(R.string.theme_system_default)
            else -> context.resources.getString(R.string.theme_light)
        }
}
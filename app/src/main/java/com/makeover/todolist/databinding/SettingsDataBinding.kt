package com.makeover.todolist.databinding

import android.app.Activity
import android.content.Context
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
    private var settingsViewModel: SettingsViewModel? = null

    fun setActivity(activity: Activity?, settingsViewModel: SettingsViewModel) {
        this.activity = activity
        this.settingsViewModel = settingsViewModel
    }

    fun changeTheme() {
        val choices: Array<String> = context.resources.getStringArray(R.array.choose_theme_choices)
        val selectedMode = SharedPreferenceManager.getIntValue(Constants.DAY_NIGHT_MODE)

        alertDialog.showAlertDialog(activity, context.resources.getString(R.string.choose_theme),
            choices, selectedMode,
            context.resources.getString(R.string.positive_button),
            context.resources.getString(R.string.negative_button),
            object : AlertDialogListener {
                override fun selectedItem(position: Int) {
                    SharedPreferenceManager.setIntValue(Constants.DAY_NIGHT_MODE, position)
                    AppCompatDelegate.setDefaultNightMode(AppUtils.getNightMode())
                    settingsViewModel?.getSelectedTheme()
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
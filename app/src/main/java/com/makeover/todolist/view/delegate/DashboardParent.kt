package com.makeover.todolist.view.delegate

import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.makeover.todolist.R
import com.makeover.todolist.`interface`.ConfirmationAlertDialogListener
import com.makeover.todolist.databinding.ActivityDashboardBinding
import com.makeover.todolist.helper.gone
import com.makeover.todolist.helper.hide
import com.makeover.todolist.helper.show
import com.makeover.todolist.view.BottomSheetCreateTaskFragment
import com.makeover.todolist.view.customviews.AlertDialogView
import com.makeover.todolist.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class DashboardParent : AppCompatActivity() {

    protected lateinit var navController: NavController
    protected var actionModeMenu: Menu? = null
    protected var _bindingDashboardActivity: ActivityDashboardBinding? = null
    protected val bindingDashboardActivity get() = _bindingDashboardActivity!!

    @Inject
    lateinit var alertDialogView: AlertDialogView

    private val dashboardViewModel: DashboardViewModel by viewModels()

    protected fun showBottomSheetFragment(isTask: Boolean, isEdit: Boolean) {
        supportFragmentManager.let {
            BottomSheetCreateTaskFragment.newInstance(
                isTask,
                isEdit
            ).apply {
                show(it, tag)
            }
        }
    }

    protected fun setUpMenu() {
        val categoryDelete = actionModeMenu?.findItem(R.id.delete_category)
        val categoryEdit = actionModeMenu?.findItem(R.id.edit_category)
        val taskDelete = actionModeMenu?.findItem(R.id.delete_task)
        val taskEdit = actionModeMenu?.findItem(R.id.edit_task)

        when (navController.currentDestination?.id) {
            R.id.task_fragment -> {
                categoryDelete?.show()
                categoryEdit?.show()
                taskDelete?.hide()
                taskEdit?.hide()

                bindingDashboardActivity.bottomNavView.gone()
                bindingDashboardActivity.newTaskFab.show()
                bindingDashboardActivity.newTaskFab.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_create_task
                    )
                )
            }
            R.id.taskDetailsFragment -> {
                categoryDelete?.hide()
                categoryEdit?.hide()
                taskDelete?.show()
                taskEdit?.show()

                bindingDashboardActivity.bottomNavView.gone()
                bindingDashboardActivity.newTaskFab.gone()
            }
            else -> {
                categoryDelete?.hide()
                categoryEdit?.hide()
                taskDelete?.hide()
                taskEdit?.hide()

                bindingDashboardActivity.bottomNavView.show()
                bindingDashboardActivity.newTaskFab.show()
                bindingDashboardActivity.newTaskFab.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_create_category
                    )
                )
            }
        }
    }

    protected fun setObservers(){
        dashboardViewModel.editTask.observe(this, {
            if (it)
                showBottomSheetFragment(isTask = true, isEdit = true)
        })
    }

    protected fun deleteCategory() {
        alertDialogView.showConfirmationDialog(this,
            getString(R.string.delete_category),
            getString(R.string.delete_category_desc),
            getString(R.string.positive_button),
            getString(R.string.negative_button),
            object : ConfirmationAlertDialogListener {
                override fun onConfirmation(isSuccess: Boolean) {
                    dashboardViewModel.deleteCategory()
                    onBackPressed()
                }
            })
    }

    protected fun deleteTask() {
        alertDialogView.showConfirmationDialog(this,
            getString(R.string.delete_task),
            getString(R.string.delete_task_desc),
            getString(R.string.positive_button),
            getString(R.string.negative_button),
            object : ConfirmationAlertDialogListener {
                override fun onConfirmation(isSuccess: Boolean) {
                    dashboardViewModel.deleteTask()
                    onBackPressed()
                }
            })
    }
}
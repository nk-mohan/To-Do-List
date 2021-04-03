package com.makeover.todolist.view.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.makeover.todolist.R
import com.makeover.todolist.databinding.ActivityDashboardBinding
import com.makeover.todolist.helper.bindView
import com.makeover.todolist.utils.Constants
import com.makeover.todolist.utils.SharedPreferenceManager
import com.makeover.todolist.view.BottomSheetCreateTaskFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private var _bindingDashboardActivity: ActivityDashboardBinding? = null
    private val bindingDashboardActivity get() = _bindingDashboardActivity!!

    private val newTaskFab: FloatingActionButton by bindView(R.id.newTaskFab)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _bindingDashboardActivity = ActivityDashboardBinding.inflate(layoutInflater)

        setContentView(bindingDashboardActivity.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_category, R.id.navigation_dashboard, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bindingDashboardActivity.navView.setupWithNavController(navController)

        if (SharedPreferenceManager.getBooleanValue(Constants.DAY_NIGHT_MODE_UPDATED)) {
            SharedPreferenceManager.setBooleanValue(Constants.DAY_NIGHT_MODE_UPDATED, false)
            navController.navigate(R.id.navigation_settings)
        }

        setUpViewActions()
    }

    private fun setUpViewActions() {
        newTaskFab.setOnClickListener {
            supportFragmentManager.let {
                BottomSheetCreateTaskFragment.newInstance(Bundle()).apply {
                    show(it, tag)
                }
            }
        }
    }
}
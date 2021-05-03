package com.makeover.todolist.view.dashboard

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.makeover.todolist.R
import com.makeover.todolist.databinding.ActivityDashboardBinding
import com.makeover.todolist.view.delegate.DashboardParent


class DashboardActivity : DashboardParent() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _bindingDashboardActivity = ActivityDashboardBinding.inflate(layoutInflater)

        setContentView(bindingDashboardActivity.root)

        setUpNavigation()
        setUpViewActions()
        setObservers()
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_category, R.id.navigation_dashboard, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bindingDashboardActivity.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            setUpMenu()
        }

        supportActionBar
    }

    private fun setUpViewActions() {
        bindingDashboardActivity.newTaskFab.setOnClickListener {
            showBottomSheetFragment(
                navController.currentDestination?.id == R.id.task_fragment,
                false
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.header_menu, menu)
        actionModeMenu = menu
        setUpMenu()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.edit_category -> {
                showBottomSheetFragment(isTask = false, isEdit = true)
            }
            R.id.delete_category -> {
                deleteCategory()
            }
            R.id.edit_task -> {
                showBottomSheetFragment(isTask = true, isEdit = true)
            }
            R.id.delete_task -> {
                deleteTask()
            }
        }
        return super.onOptionsItemSelected(item)

    }
}
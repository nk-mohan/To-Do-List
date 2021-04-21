package com.makeover.todolist.view.dashboard.ui.taskdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.makeover.todolist.databinding.FragmentTaskDetailsBinding
import com.makeover.todolist.view.dashboard.DashboardActivity
import com.makeover.todolist.view.delegate.ViewBindingHolder
import com.makeover.todolist.view.delegate.ViewBindingHolderImpl
import com.makeover.todolist.viewmodel.DashboardViewModel

class TaskDetailsFragment : Fragment(),
    ViewBindingHolder<FragmentTaskDetailsBinding> by ViewBindingHolderImpl() {

    private var taskId: Int = 0

    private var _taskDetailsBinding: FragmentTaskDetailsBinding? = null
    private val taskDetailsBinding get() = _taskDetailsBinding!!

    private val dashboardViewModel: DashboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = initBinding(FragmentTaskDetailsBinding.inflate(layoutInflater), this) {
        _taskDetailsBinding = requireBinding()

        taskId = TaskDetailsFragmentArgs.fromBundle(requireArguments()).taskId

        dashboardViewModel.taskId = taskId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel.getTask(taskId)

        setObservers()
    }

    private fun setObservers() {
        dashboardViewModel.task.observe(viewLifecycleOwner, { task ->
            taskDetailsBinding.task = task
            (activity as DashboardActivity).supportActionBar?.let {
                it.apply {
                    title = task.title
                }
            }
        })
    }

}
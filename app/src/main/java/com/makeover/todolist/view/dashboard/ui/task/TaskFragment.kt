package com.makeover.todolist.view.dashboard.ui.task

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.makeover.todolist.databinding.FragmentTaskBinding
import com.makeover.todolist.room.model.Task
import com.makeover.todolist.view.dashboard.DashboardActivity
import com.makeover.todolist.view.delegate.ViewBindingHolder
import com.makeover.todolist.view.delegate.ViewBindingHolderImpl
import com.makeover.todolist.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment(), ViewBindingHolder<FragmentTaskBinding> by ViewBindingHolderImpl(),
    TaskAdapter.TaskAdapterClickListener {

    private var _taskFragmentBinding: FragmentTaskBinding? = null
    private val taskFragmentBinding get() = _taskFragmentBinding!!

    private val dashboardViewModel: DashboardViewModel by activityViewModels()

    private var categoryId: Int = 0

    private val taskAdapter: TaskAdapter by lazy {
        TaskAdapter(dashboardViewModel.taskListByCategoryAdapter, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = initBinding(FragmentTaskBinding.inflate(layoutInflater), this) {
        _taskFragmentBinding = requireBinding()

        categoryId = TaskFragmentArgs.fromBundle(requireArguments()).categoryId
        dashboardViewModel.categoryId = categoryId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setObservers()
        dashboardViewModel.getCategory(categoryId)
        dashboardViewModel.getTaskListById(categoryId)
    }

    private fun initViews() {
        taskFragmentBinding.taskRecyclerView.apply {
            adapter = taskAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    private fun setObservers() {
        dashboardViewModel.category.observe(viewLifecycleOwner, { category ->
            (activity as DashboardActivity).supportActionBar?.let {
                it.apply {
                    title = category.name
                }
            }
        })

        dashboardViewModel.taskDiffResult.observe(viewLifecycleOwner, { diffUtilResult ->
            // Save Current Scroll state to retain scroll position after DiffUtils Applied
            val previousState =
                taskFragmentBinding.taskRecyclerView.layoutManager?.onSaveInstanceState() as Parcelable
            diffUtilResult.dispatchUpdatesTo(taskAdapter)
            taskFragmentBinding.taskRecyclerView.layoutManager?.onRestoreInstanceState(previousState)
        })
    }

    override fun onTaskClicked(task: Task) {
        dashboardViewModel.taskId = task.id!!
        val action = TaskFragmentDirections.actionTaskFragmentToTaskDetailsFragment(task.id!!)
        findNavController().navigate(action)
    }
}
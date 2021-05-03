package com.makeover.todolist.view.dashboard.ui.taskdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.makeover.todolist.`interface`.DateAndTimePickerListener
import com.makeover.todolist.databinding.FragmentTaskDetailsBinding
import com.makeover.todolist.helper.gone
import com.makeover.todolist.helper.show
import com.makeover.todolist.room.model.Task
import com.makeover.todolist.utils.AppUtils
import com.makeover.todolist.view.BottomSheetCreateTaskFragment
import com.makeover.todolist.view.customviews.ScheduleDateFragment
import com.makeover.todolist.view.dashboard.DashboardActivity
import com.makeover.todolist.view.delegate.ViewBindingHolder
import com.makeover.todolist.view.delegate.ViewBindingHolderImpl
import com.makeover.todolist.viewmodel.DashboardViewModel

class TaskDetailsFragment : Fragment(),
    ViewBindingHolder<FragmentTaskDetailsBinding> by ViewBindingHolderImpl(), View.OnClickListener {

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

        setUpViews()
        setObservers()
    }

    private fun setUpViews() {
        taskDetailsBinding.taskDescriptionLayout.setOnClickListener(this)
        taskDetailsBinding.scheduleTaskLayout.setOnClickListener(this)
        taskDetailsBinding.scheduleTimeLayout.cancelSchedule.setOnClickListener(this)
    }

    private fun setObservers() {
        dashboardViewModel.task.observe(viewLifecycleOwner, { task ->
            taskDetailsBinding.task = task

            setTaskDetails(task)
        })
    }

    private fun setTaskDetails(task: Task?) {
        task?.let {
            (activity as DashboardActivity).supportActionBar?.let {
                it.apply {
                    title = task.title
                }
            }

            if (task.date == null) {
                taskDetailsBinding.scheduleTimeHint.show()
                taskDetailsBinding.scheduleTimeLayout.scheduleTimeLayout.gone()
            } else {
                taskDetailsBinding.scheduleTimeHint.gone()
                taskDetailsBinding.scheduleTimeLayout.scheduleTimeLayout.show()
                taskDetailsBinding.scheduleTimeLayout.scheduledTime.text =
                    AppUtils.getDateAndTimeString(
                        requireContext(),
                        task.date,
                        task.hour,
                        task.minute
                    )
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            taskDetailsBinding.taskDescriptionLayout -> {
                dashboardViewModel.editTask()
            }
            taskDetailsBinding.scheduleTaskLayout -> {
                ScheduleDateFragment(dashboardViewModel.task.value?.date,
                    dashboardViewModel.task.value?.hour,
                    dashboardViewModel.task.value?.minute, object : DateAndTimePickerListener {
                        override fun onSelectedDateAndTime(date: Long, hour: Int, minute: Int) {
                            taskDetailsBinding.scheduleTimeHint.gone()
                            taskDetailsBinding.scheduleTimeLayout.root.show()
                            taskDetailsBinding.scheduleTimeLayout.scheduledTime.text =
                                AppUtils.getDateAndTimeString(requireContext(), date, hour, minute)
                            dashboardViewModel.updateTaskTime(date, hour, minute, requireContext())
                        }
                    }).show(parentFragmentManager, ScheduleDateFragment.TAG)
            }
            taskDetailsBinding.scheduleTimeLayout.cancelSchedule -> {
                taskDetailsBinding.scheduleTimeHint.show()
                taskDetailsBinding.scheduleTimeLayout.scheduleTimeLayout.gone()
                dashboardViewModel.cancelScheduledTask(requireContext())
            }
        }
    }

}
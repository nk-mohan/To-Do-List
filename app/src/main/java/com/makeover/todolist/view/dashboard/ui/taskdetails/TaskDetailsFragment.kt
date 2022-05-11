package com.makeover.todolist.view.dashboard.ui.taskdetails

import android.animation.LayoutTransition
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.makeover.todolist.`interface`.DateAndTimePickerListener
import com.makeover.todolist.databinding.FragmentTaskDetailsBinding
import com.makeover.todolist.helper.gone
import com.makeover.todolist.helper.show
import com.makeover.todolist.helper.showKeyBoard
import com.makeover.todolist.room.TaskRepository
import com.makeover.todolist.room.model.Task
import com.makeover.todolist.utils.AppUtils
import com.makeover.todolist.view.customviews.ScheduleDateFragment
import com.makeover.todolist.view.dashboard.DashboardActivity
import com.makeover.todolist.view.delegate.ViewBindingHolder
import com.makeover.todolist.view.delegate.ViewBindingHolderImpl
import com.makeover.todolist.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class TaskDetailsFragment : Fragment(),
    ViewBindingHolder<FragmentTaskDetailsBinding> by ViewBindingHolderImpl(), View.OnClickListener,
    SubTaskAdapter.SubTaskOnClickListener {

    private var taskId: Int = 0

    private var _taskDetailsBinding: FragmentTaskDetailsBinding? = null
    private val taskDetailsBinding get() = _taskDetailsBinding!!

    private val dashboardViewModel: DashboardViewModel by activityViewModels()

    @Inject
    lateinit var taskRepository: TaskRepository

    private val subTaskAdapter: SubTaskAdapter by lazy {
        SubTaskAdapter(dashboardViewModel.subTaskListAdapter, this, requireContext())
    }

    private val subTaskCompletedAdapter: SubTaskAdapter by lazy {
        SubTaskAdapter(dashboardViewModel.subTaskCompletedListAdapter, this, requireContext())
    }

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
        dashboardViewModel.getTaskDetails(taskId)

        initViews()
        setObservers()
    }

    private fun initViews() {
        taskDetailsBinding.taskDescriptionLayout.setOnClickListener(this)
        taskDetailsBinding.scheduleTaskLayout.setOnClickListener(this)
        taskDetailsBinding.scheduleTimeLayout.cancelSchedule.setOnClickListener(this)
        taskDetailsBinding.subTaskHint.setOnClickListener(this)

        taskDetailsBinding.subTaskRecyclerView.apply {
            subTaskAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter = subTaskAdapter
        }

        taskDetailsBinding.subTaskCompletedRecyclerView.apply {
            subTaskAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter = subTaskCompletedAdapter
        }

        val transition = LayoutTransition()
        transition.enableTransitionType(LayoutTransition.CHANGING)
        transition.setStartDelay(LayoutTransition.CHANGING, 500)
        taskDetailsBinding.subTaskLayout.layoutTransition = transition
    }

    private fun setObservers() {
        dashboardViewModel.taskDetails.observe(viewLifecycleOwner, { task ->
            taskDetailsBinding.task = task

            setTaskDetails(task)
        })
        dashboardViewModel.subTaskDiffResult.observe(viewLifecycleOwner, { diffUtilResult ->
            diffUtilResult.dispatchUpdatesTo(subTaskAdapter)
            updateHintPosition()
        })

        dashboardViewModel.subTaskCompletedDiffResult.observe(viewLifecycleOwner, { diffUtilResult ->
            diffUtilResult.dispatchUpdatesTo(subTaskCompletedAdapter)
        })

        CoroutineScope(Dispatchers.IO).launch {
            observeSubTaskList()
            observeCompletedSubTaskList()
        }
    }

    suspend fun observeSubTaskList() = withContext(Dispatchers.Main) {
        taskRepository.getSubTaskList(taskId).observe(viewLifecycleOwner, { list ->
            dashboardViewModel.subTaskDetails.clear()
            dashboardViewModel.subTaskDetails.addAll(list)
            dashboardViewModel.getSubTaskDiffResult()
        })
    }

    suspend fun observeCompletedSubTaskList() = withContext(Dispatchers.Main) {
        taskRepository.getCompletedSubTaskList(taskId).observe(viewLifecycleOwner, { list ->
            dashboardViewModel.subTaskCompletedDetails.clear()
            dashboardViewModel.subTaskCompletedDetails.addAll(list)
            dashboardViewModel.getSubTaskCompletedDiffResult()
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

    private fun updateHintPosition() {
        if (dashboardViewModel.subTaskListAdapter.size > 0)
            taskDetailsBinding.subTaskHint.setPaddingRelative(
                AppUtils.dp(30f, requireContext()),
                AppUtils.dp(10f, requireContext()),
                0,
                0
            )
        else
            taskDetailsBinding.subTaskHint.setPaddingRelative(
                0,
                AppUtils.dp(2f, requireContext()),
                0,
                0
            )
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
            taskDetailsBinding.subTaskHint -> {
                subTaskAdapter.setLastPositionFocusable(true)
                dashboardViewModel.createSubTask()
            }
        }
    }

    override fun deleteSubTask(id: Int) {
       // if (dashboardViewModel.subTaskListAdapter.size > index) {
            dashboardViewModel.deleteSubTask(id)
       // }
    }

    override fun completeSubTask(id: Int) {
        dashboardViewModel.updateSubTaskCompletion(id, true)
    }

    override fun updateSubTaskTitle(id: Int, title: String) {
        dashboardViewModel.updateSubTaskTitle(id, title)
    }

    override fun openKeyBoard(subTaskTitle: TextInputEditText) {
        with(subTaskTitle) {
            requestFocus()
            postDelayed({
                showKeyBoard(this)
            }, 50)
        }
        subTaskAdapter.setLastPositionFocusable(false)
    }

}
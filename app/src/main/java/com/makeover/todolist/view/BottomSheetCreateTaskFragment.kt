package com.makeover.todolist.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.makeover.todolist.R
import com.makeover.todolist.`interface`.DateAndTimePickerListener
import com.makeover.todolist.databinding.BottomSheetCreateTaskBinding
import com.makeover.todolist.helper.gone
import com.makeover.todolist.helper.show
import com.makeover.todolist.helper.showKeyBoard
import com.makeover.todolist.helper.views.CustomToast
import com.makeover.todolist.utils.AppConstants
import com.makeover.todolist.utils.AppUtils
import com.makeover.todolist.view.customviews.ScheduleDateFragment
import com.makeover.todolist.view.delegate.ViewBindingHolder
import com.makeover.todolist.view.delegate.ViewBindingHolderImpl
import com.makeover.todolist.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetCreateTaskFragment : BottomSheetDialogFragment(), View.OnClickListener,
    ViewBindingHolder<BottomSheetCreateTaskBinding> by ViewBindingHolderImpl() {

    private val viewModel: DashboardViewModel by activityViewModels()

    private var _createTaskBinding: BottomSheetCreateTaskBinding? = null
    private val createTaskBinding get() = _createTaskBinding!!

    private var isCreateTask = true
    private var isEdit = false

    private var currentSelectedDate: Long? = null
    private var currentSelectedHour: Int? = null
    private var currentSelectedMinute: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = initBinding(BottomSheetCreateTaskBinding.inflate(layoutInflater), this) {
        _createTaskBinding = requireBinding()
        arguments?.let {
            isCreateTask = it.getBoolean(AppConstants.CREATE_TASK)
            isEdit = it.getBoolean(AppConstants.IS_EDIT)
        }
        setUpViews()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setObservers()
    }

    private fun setUpViews() {
        if (isCreateTask) {
            setUpTaskView()
        } else {
            setUpCategoryView()
        }

        //Setup Key board when bottom sheet open
        with(createTaskBinding.taskNameEditText) {
            requestFocus()
            postDelayed({
                showKeyBoard(this)
            }, 50)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    createTaskBinding.btnCreateTaskCategory.isActivated = !s.isNullOrBlank()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    /* No Implementation needed */
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    /* No Implementation needed */
                }

            })
        }
        createTaskBinding.scheduleTask.setOnClickListener(this)
        createTaskBinding.taskDescription.setOnClickListener(this)
        createTaskBinding.btnCreateTaskCategory.setOnClickListener(this)
        createTaskBinding.scheduleTimeLayout.cancelSchedule.setOnClickListener(this)
        createTaskBinding.scheduleTimeLayout.scheduleTimeLayout.setOnClickListener(this)
    }

    private fun setUpCategoryView() {
        createTaskBinding.scheduleTask.gone()
        createTaskBinding.taskDescription.gone()
        createTaskBinding.taskNameEditText.hint = getString(R.string.new_category)
        if (isEdit) {
            viewModel.getCategory(viewModel.categoryId)
            createTaskBinding.btnCreateTaskCategory.text = getString(R.string.update)
        }
    }

    private fun setUpTaskView() {
        createTaskBinding.scheduleTask.show()
        createTaskBinding.taskDescription.show()
        createTaskBinding.taskNameEditText.hint = getString(R.string.new_task)
        if (isEdit) {
            viewModel.getTask(viewModel.taskId)
            createTaskBinding.btnCreateTaskCategory.text = getString(R.string.update)
        }
    }


    private fun setObservers() {
        viewModel.category.observe(viewLifecycleOwner, {
            if (isEdit && !isCreateTask)
                with(createTaskBinding.taskNameEditText) {
                    setText(it.name)
                    setSelection(text.length)
                }
        })

        viewModel.task.observe(viewLifecycleOwner, { task ->
            if (isEdit && isCreateTask) {
                with(createTaskBinding.taskNameEditText) {
                    setText(task.title)
                    setSelection(text.length)
                }
                with(createTaskBinding.taskDescriptionEditText) {
                    show()
                    setText(task.description)
                }
                task.date?.let {
                    currentSelectedDate = task.date
                    currentSelectedHour = task.hour
                    currentSelectedMinute = task.minute
                    createTaskBinding.scheduleTimeLayout.root.show()
                    createTaskBinding.scheduleTimeLayout.scheduledTime.text =
                        AppUtils.getDateAndTimeString(
                            requireContext(),
                            task.date,
                            task.hour,
                            task.minute
                        )
                }

            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(isTask: Boolean, isEdit: Boolean): BottomSheetCreateTaskFragment =
            BottomSheetCreateTaskFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(AppConstants.CREATE_TASK, isTask)
                    putBoolean(AppConstants.IS_EDIT, isEdit)
                }
            }
    }

    override fun onClick(v: View?) {
        when (v) {
            createTaskBinding.btnCreateTaskCategory -> {
                if (isEdit) {
                    update()
                } else {
                    create()
                }
            }
            createTaskBinding.taskDescription -> {
                with(createTaskBinding.taskDescriptionEditText) {
                    show()
                    requestFocus()
                    setSelection(text.length)
                }
            }
            createTaskBinding.scheduleTask, createTaskBinding.scheduleTimeLayout.scheduleTimeLayout -> {
                ScheduleDateFragment(currentSelectedDate,
                    currentSelectedHour,
                    currentSelectedMinute, object : DateAndTimePickerListener {
                        override fun onSelectedDateAndTime(date: Long, hour: Int, minute: Int) {
                            currentSelectedDate = date
                            currentSelectedHour = hour
                            currentSelectedMinute = minute
                            createTaskBinding.scheduleTimeLayout.root.show()
                            createTaskBinding.scheduleTimeLayout.scheduledTime.text =
                                AppUtils.getDateAndTimeString(requireContext(), date, hour, minute)
                        }
                    }).show(parentFragmentManager, ScheduleDateFragment.TAG)
            }
            createTaskBinding.scheduleTimeLayout.cancelSchedule -> {
                createTaskBinding.scheduleTimeLayout.root.gone()
                currentSelectedDate = null
                currentSelectedHour = null
                currentSelectedMinute = null
            }
        }
    }

    private fun create() {
        if (isCreateTask)
            createTask()
        else
            createCategory()
    }

    private fun createTask() {
        if (isValidTextToCreateTaskOrCategory()) {
            viewModel.createTask(
                createTaskBinding.taskNameEditText.text.toString(),
                createTaskBinding.taskDescriptionEditText.text.toString(),
                currentSelectedDate,
                currentSelectedHour,
                currentSelectedMinute,
                context
            )
            dismiss()
        }
    }

    private fun createCategory() {
        if (isValidTextToCreateTaskOrCategory()) {
            viewModel.createCategory(createTaskBinding.taskNameEditText.text.toString())
            dismiss()
        }
    }

    private fun update() {
        if (isCreateTask)
            updateTask()
        else
            updateCategory()
    }

    private fun updateTask() {
        if (isValidTextToCreateTaskOrCategory()) {
            viewModel.updateTask(
                createTaskBinding.taskNameEditText.text.toString(),
                createTaskBinding.taskDescriptionEditText.text.toString(),
                currentSelectedDate,
                currentSelectedHour,
                currentSelectedMinute,
                requireContext()
            )
            dismiss()
        }
    }

    private fun updateCategory() {
        if (isValidTextToCreateTaskOrCategory()) {
            viewModel.updateCategory(createTaskBinding.taskNameEditText.text.toString())
            dismiss()
        }
    }

    private fun isValidTextToCreateTaskOrCategory(): Boolean {
        if (createTaskBinding.taskNameEditText.text.isNullOrBlank()) {
            CustomToast.showShortToast(
                requireContext(),
                if (isCreateTask) getString(R.string.alert_task_title_empty) else getString(R.string.alert_category_empty)
            )
            return false
        }
        return true
    }
}
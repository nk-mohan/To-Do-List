package com.makeover.todolist.view.customviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.makeover.todolist.`interface`.DateAndTimePickerListener
import com.makeover.todolist.`interface`.TimePickerListener
import com.makeover.todolist.databinding.AlertScheduleDateBinding
import com.makeover.todolist.utils.AppUtils
import com.makeover.todolist.view.delegate.ViewBindingHolder
import com.makeover.todolist.view.delegate.ViewBindingHolderImpl
import java.util.*

class ScheduleDateFragment(
    private val selectedDate: Long?,
    private val selectedHour: Int?,
    private val selectedMinute: Int?,
    private val dateAndTimePickerListener: DateAndTimePickerListener
) : DialogFragment(),
    ViewBindingHolder<AlertScheduleDateBinding> by ViewBindingHolderImpl() {

    companion object {
        const val TAG = "ScheduleDateFragment"
    }

    private var _alertScheduleDateBinding: AlertScheduleDateBinding? = null
    private val alertScheduleDateBinding: AlertScheduleDateBinding get() = _alertScheduleDateBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = initBinding(AlertScheduleDateBinding.inflate(layoutInflater), this) {
        _alertScheduleDateBinding = requireBinding()

        selectedDate?.let {
            alertScheduleDateBinding.calendarView.setDate(it, true, true)
        }

        var currentSelectedDate = alertScheduleDateBinding.calendarView.date
        var currentSelectedHour: Int =
            selectedHour ?: Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        var currentSelectedMinute: Int =
            selectedMinute ?: Calendar.getInstance().get(Calendar.MINUTE)

        setHourAndMinute(selectedHour, selectedMinute)

        alertScheduleDateBinding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val customCalendar = Calendar.getInstance()
            customCalendar.set(year, month, dayOfMonth)
            currentSelectedDate = customCalendar.timeInMillis
        }
        alertScheduleDateBinding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
        alertScheduleDateBinding.btnDone.setOnClickListener {
            dateAndTimePickerListener.onSelectedDateAndTime(
                currentSelectedDate,
                currentSelectedHour,
                currentSelectedMinute
            )
            dialog?.dismiss()
        }
        alertScheduleDateBinding.scheduleTimeLayout.setOnClickListener {
            ScheduleTimeFragment(currentSelectedHour,
                currentSelectedMinute, object : TimePickerListener {
                    override fun onSelectedTime(hour: Int, minute: Int) {
                        currentSelectedHour = hour
                        currentSelectedMinute = minute
                        setHourAndMinute(hour, minute)
                    }
                }).show(parentFragmentManager, ScheduleTimeFragment.TAG)
        }
    }

    private fun setHourAndMinute(hour: Int?, minute: Int?) {
        hour?.let {
            alertScheduleDateBinding.selectedTime.text =
                AppUtils.getTimeString(requireContext(), hour, minute!!)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}
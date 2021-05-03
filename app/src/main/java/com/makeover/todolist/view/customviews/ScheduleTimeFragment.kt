package com.makeover.todolist.view.customviews

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.makeover.todolist.`interface`.TimePickerListener
import com.makeover.todolist.databinding.AlertScheduleTimeBinding
import com.makeover.todolist.view.delegate.ViewBindingHolder
import com.makeover.todolist.view.delegate.ViewBindingHolderImpl

@Suppress("DEPRECATION")
class ScheduleTimeFragment(
    private val selectedHour: Int,
    private val selectedMinute: Int,
    private val timePickerListener: TimePickerListener
) : DialogFragment(),
    ViewBindingHolder<AlertScheduleTimeBinding> by ViewBindingHolderImpl() {

    companion object {
        const val TAG = "ScheduleTimeFragment"
    }

    private var _alertScheduleTimeBinding: AlertScheduleTimeBinding? = null
    private val alertScheduleTimeBinding: AlertScheduleTimeBinding get() = _alertScheduleTimeBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = initBinding(AlertScheduleTimeBinding.inflate(layoutInflater), this) {
        _alertScheduleTimeBinding = requireBinding()

        val currentSelectedHour: Int = selectedHour
        val currentSelectedMinute: Int = selectedMinute

        with(alertScheduleTimeBinding.timePicker) {
            setIs24HourView(false)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alertScheduleTimeBinding.timePicker.hour = currentSelectedHour
            alertScheduleTimeBinding.timePicker.minute = currentSelectedMinute
        } else {
            alertScheduleTimeBinding.timePicker.currentHour = currentSelectedHour
            alertScheduleTimeBinding.timePicker.currentMinute = currentSelectedMinute
        }
        alertScheduleTimeBinding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
        alertScheduleTimeBinding.btnDone.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePickerListener.onSelectedTime(
                    alertScheduleTimeBinding.timePicker.hour,
                    alertScheduleTimeBinding.timePicker.minute
                )
            } else {
                timePickerListener.onSelectedTime(
                    alertScheduleTimeBinding.timePicker.currentHour,
                    alertScheduleTimeBinding.timePicker.currentMinute
                )
            }
            dialog?.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

}
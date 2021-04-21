package com.makeover.todolist.view.customviews

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.makeover.todolist.`interface`.DateAndTimePickerListener
import java.util.*
import javax.inject.Inject


class DateAndTimePicker @Inject constructor() {

    fun showDatePicker(
        fragmentManager: FragmentManager,
        currentSelectedDate: Long?,
        selectedHour: Int?,
        selectedMinute: Int?,
        dateAndTimePickerListener: DateAndTimePickerListener
    ) {
        val dateBuilder = MaterialDatePicker.Builder.datePicker()

        val selectedDateInMillis = currentSelectedDate ?: System.currentTimeMillis()

        val today = MaterialDatePicker.todayInUtcMilliseconds()


        val constraints = CalendarConstraints.Builder()

        val dateValidator: DateValidator = DateValidatorPointForward.now()
        constraints.setValidator(dateValidator) // Previous dates hide
        constraints.setStart(today) // Calender start from set day of the month

        dateBuilder.apply {
            setSelection(selectedDateInMillis)
            setCalendarConstraints(constraints.build())
            setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            setTitleText("Schedule Task")
        }

        val datePicker = dateBuilder.build()

        datePicker.addOnPositiveButtonClickListener {
            showTimePicker(
                fragmentManager,
                it,
                selectedHour,
                selectedMinute,
                dateAndTimePickerListener
            )
        }

        datePicker.show(fragmentManager, datePicker.toString())
    }

    private fun showTimePicker(
        fragmentManager: FragmentManager,
        selectedDate: Long,
        selectedHour: Int?,
        selectedMinute: Int?,
        dateAndTimePickerListener: DateAndTimePickerListener
    ) {
        val currentSelectedHour: Int =
            selectedHour ?: Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val currentSelectedMinute: Int =
            selectedMinute ?: Calendar.getInstance().get(Calendar.MINUTE)
        val timeBuilder = MaterialTimePicker.Builder()
        timeBuilder.apply {
            setTimeFormat(TimeFormat.CLOCK_24H)
            setTitleText("Schedule Task")
            setHour(currentSelectedHour)
            setMinute(currentSelectedMinute)
        }
        val timePicker = timeBuilder.build()
        timePicker.addOnPositiveButtonClickListener {

            dateAndTimePickerListener.onSelectedDateAndTime(
                selectedDate,
                timePicker.hour,
                timePicker.minute
            )
        }
        timePicker.show(fragmentManager, MaterialTimePicker::class.java.canonicalName)

    }

}
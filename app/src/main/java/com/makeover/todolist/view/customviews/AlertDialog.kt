package com.makeover.todolist.view.customviews

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.makeover.todolist.R
import com.makeover.todolist.`interface`.AlertDialogListener
import javax.inject.Inject

class AlertDialog @Inject constructor() {

    fun showAlertDialog(
        activity: Activity?,
        title: String,
        choices: Array<String>,
        selectedPosition: Int,
        positiveButtonName: String,
        negativeButtonName: String,
        listener: AlertDialogListener
    ) {
        activity?.let {
            var updatedPosition = selectedPosition
            val dialogBuilder = AlertDialog.Builder(it, R.style.AppCompatAlertDialogStyle)
            dialogBuilder.setTitle(title)
            dialogBuilder.setSingleChoiceItems(choices, selectedPosition) { dialog, selectedItem ->
                updatedPosition = selectedItem
            }
            dialogBuilder.setPositiveButton(positiveButtonName) { dialog, selectedItem ->
                listener.selectedItem(updatedPosition)
                dialog.dismiss()
            }
            dialogBuilder.setNegativeButton(negativeButtonName) { dialog, selectedItem ->
                dialog.dismiss()
            }
            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        }
    }
}
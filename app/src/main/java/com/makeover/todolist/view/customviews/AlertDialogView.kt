package com.makeover.todolist.view.customviews

import android.app.Activity
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.makeover.todolist.R
import com.makeover.todolist.`interface`.ChooseOneAlertDialogListener
import com.makeover.todolist.`interface`.ConfirmationAlertDialogListener
import javax.inject.Inject

class AlertDialogView @Inject constructor() {

    fun showChooseOneAlertDialog(
        activity: Activity?,
        title: String,
        choices: Array<String>,
        selectedPosition: Int,
        positiveButtonName: String,
        negativeButtonName: String,
        listener: ChooseOneAlertDialogListener
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

    fun showConfirmationDialog(
        activity: Activity?,
        title: String,
        description: String,
        positiveButtonName: String,
        negativeButtonName: String,
        listener: ConfirmationAlertDialogListener
    ) {
        activity?.let {
            val dialogBuilder = AlertDialog.Builder(it, R.style.AppCompatAlertDialogStyle)
            dialogBuilder.setTitle(title)
            dialogBuilder.setMessage(description)
            dialogBuilder.setNegativeButton(negativeButtonName) { dialog, _ ->
                dialog.dismiss()
            }
            dialogBuilder.setPositiveButton(positiveButtonName) { dialog, _ ->
                dialog.dismiss()
                listener.onConfirmation(true)
            }
            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        }
    }
}
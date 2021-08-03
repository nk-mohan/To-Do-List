package com.makeover.todolist.view.dashboard.ui.taskdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.makeover.todolist.R
import com.makeover.todolist.databinding.RowSubTaskBinding
import com.makeover.todolist.room.model.SubTask

class SubTaskAdapter(
    private var taskList: List<SubTask>,
    private val subTaskOnClickListener: SubTaskOnClickListener,
    private val context: Context
) : RecyclerView.Adapter<SubTaskAdapter.SubTaskViewHolder>() {

    private var lastPositionFocusable = false

    inner class SubTaskViewHolder(val subTaskViewBinding: RowSubTaskBinding) :
        RecyclerView.ViewHolder(subTaskViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTaskViewHolder {
        return SubTaskViewHolder(
            RowSubTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SubTaskViewHolder, position: Int) {
        holder.subTaskViewBinding.subTask = taskList[position]

        holder.subTaskViewBinding.deleteSubTask.setOnClickListener {
            subTaskOnClickListener.deleteSubTask(position)
        }

        holder.subTaskViewBinding.radioButton.setOnClickListener {
            if (holder.subTaskViewBinding.radioButton.isChecked) {
                subTaskOnClickListener.completeSubTask(taskList[position].id!!)
                holder.subTaskViewBinding.subTaskTitle.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.app_color
                    )
                )
            }
        }

        holder.subTaskViewBinding.subTaskTitle.addTextChangedListener {
            it?.let {
                if (holder.subTaskViewBinding.subTaskTitle.isCursorVisible) {
                    taskList[position].title = it.toString()
                    subTaskOnClickListener.updateSubTaskTitle(
                        taskList[position].id!!,
                        it.toString()
                    )
                }
            }
        }

        holder.subTaskViewBinding.subTaskTitle.setOnFocusChangeListener { _, hasFocus ->
            holder.subTaskViewBinding.deleteSubTask.visibility =
                if (hasFocus) View.VISIBLE else View.INVISIBLE
        }

        if (lastPositionFocusable && position == taskList.size - 1) {
            subTaskOnClickListener.openKeyBoard(holder.subTaskViewBinding.subTaskTitle)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun setLastPositionFocusable(isFocusable: Boolean) {
        lastPositionFocusable = isFocusable
    }

    interface SubTaskOnClickListener {
        fun deleteSubTask(index: Int)

        fun completeSubTask(id: Int)

        fun updateSubTaskTitle(id: Int, title: String)

        fun openKeyBoard(subTaskTitle: TextInputEditText)
    }
}
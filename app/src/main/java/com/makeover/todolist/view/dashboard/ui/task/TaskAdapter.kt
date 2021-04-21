package com.makeover.todolist.view.dashboard.ui.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makeover.todolist.databinding.RowTaskBinding
import com.makeover.todolist.room.model.Task

class TaskAdapter(
    private var taskList: List<Task>,
    private val clickListener: TaskAdapterClickListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val taskViewBinding: RowTaskBinding) :
        RecyclerView.ViewHolder(taskViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            RowTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.taskViewBinding.task = taskList[position]
        holder.taskViewBinding.clickListener = clickListener
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    interface TaskAdapterClickListener {
        fun onTaskClicked(task: Task)
    }
}
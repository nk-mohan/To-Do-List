package com.makeover.todolist.helper

import androidx.recyclerview.widget.DiffUtil
import com.makeover.todolist.room.model.Task

class TaskDiffCallback(
    private val oldList: MutableList<Task>,
    private val newList: MutableList<Task>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.id == newItem.id && oldItem.categoryId == newItem.categoryId
                && oldItem.title == newItem.title
    }
}
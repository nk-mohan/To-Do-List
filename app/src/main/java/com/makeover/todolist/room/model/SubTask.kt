package com.makeover.todolist.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sub_task")
data class SubTask(
    @ColumnInfo(name = "task_id") var taskId: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "is_completed") var isCompleted: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

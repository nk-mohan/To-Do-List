package com.makeover.todolist.room.model

import androidx.room.Embedded
import androidx.room.Relation

data class TaskDetails(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    val subTaskList: List<SubTask>
)

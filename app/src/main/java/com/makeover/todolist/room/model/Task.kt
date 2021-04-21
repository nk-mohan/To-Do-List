package com.makeover.todolist.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.makeover.todolist.utils.AppConstants

@Entity(tableName = "task")
data class Task(
    @ColumnInfo(name = "category_id") val categoryId: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "date") val date: Long?,
    @ColumnInfo(name = "hour") val hour: Int?,
    @ColumnInfo(name = "minute") val minute: Int?,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    constructor(categoryId: Int?, title: String?) :
            this(categoryId, title, AppConstants.EMPTY_STRING, null,null,null)
}

package com.makeover.todolist.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "category")
data class Category(@ColumnInfo(name = "name") val name: String?){
    @PrimaryKey(autoGenerate = true)
    var id: Int?=null
}
package com.example.android_todo_app_ultimatereo.data

import androidx.room.Entity

@Entity(tableName = "todoItems")
data class TodoItem(
    val id: String,
    val text: String,
    val priority: TodoPriority?,
    val deadline: Long?,
    var isDone: Boolean,
    val timeOfCreation: Long,
    val timeOfLastChange: Long?
)

enum class TodoPriority {
    LOW, MEDIUM, HIGH
}

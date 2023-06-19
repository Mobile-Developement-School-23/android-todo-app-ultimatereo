package com.example.android_todo_app_ultimatereo.recyclerview.data

import java.util.Date

data class TodoItem(
    val id: String,
    val text: String,
    val priority: TodoPriority?,
    val deadline: Date?,
    var isDone: Boolean,
    val timeOfCreation: Date,
    val timeOfLastChange: Date?
)

enum class TodoPriority {
    LOW, MEDIUM, HIGH
}

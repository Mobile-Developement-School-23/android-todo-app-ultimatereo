package com.example.android_todo_app_ultimatereo.recyclerview.data

import java.sql.Timestamp

data class TodoItem (
    val id : String,
    val text : String,
    val importance : TodoPriority = TodoPriority.NO,
    val deadline : Timestamp?,
    val isDone : Boolean,
    val timeOfCreation : Timestamp,
    val timeOfLastChange : Timestamp
    )

enum class TodoPriority {
    NO, LOW, MEDIUM, HIGH
}

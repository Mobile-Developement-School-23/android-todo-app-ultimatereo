package com.example.android_todo_app_ultimatereo.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_todo_app_ultimatereo.R
import com.example.android_todo_app_ultimatereo.recyclerview.data.TodoItem

class TodoItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val todoText : TextView = itemView.findViewById(R.id.todo_text)
    fun onBind(todoItem: TodoItem) {
        todoText.text = todoItem.text
    }
}

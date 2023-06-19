package com.example.android_todo_app_ultimatereo.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android_todo_app_ultimatereo.R
import com.example.android_todo_app_ultimatereo.recyclerview.data.TodoItem
import com.example.android_todo_app_ultimatereo.recyclerview.domain.CommonCallbackImpl

class TodoAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var todoItems = listOf<TodoItem>()
        set(value) {
            val callback = CommonCallbackImpl(
                oldItems = field,
                newItems = value,
                { oldItem: TodoItem, newItem -> oldItem.id == newItem.id }
            )
            field = value
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TodoItemHolder(context, layoutInflater.inflate(R.layout.todo_item, parent, false))
    }

    override fun getItemCount(): Int = todoItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TodoItemHolder) {
            holder.onBind(todoItems[position])
        }
    }
}
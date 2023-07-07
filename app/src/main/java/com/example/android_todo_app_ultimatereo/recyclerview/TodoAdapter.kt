package com.example.android_todo_app_ultimatereo.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android_todo_app_ultimatereo.R
import com.example.android_todo_app_ultimatereo.data.TodoDao
import com.example.android_todo_app_ultimatereo.data.TodoItem
class TodoAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ: AsyncListDiffer<TodoItem> = AsyncListDiffer(this, DiffCallback())
    fun submitList(list: List<TodoItem>) = differ.submitList(list)
    fun currentList(): List<TodoItem> = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val vh = TodoItemHolder(
            context,
            layoutInflater.inflate(
                R.layout.todo_item,
                parent,
                false)
        )
        return vh
    }

    override fun getItemCount(): Int = currentList().size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TodoItemHolder) {
            holder.onBind(currentList()[position])
        }
    }
    private class DiffCallback : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem) =
            oldItem == newItem
    }
}
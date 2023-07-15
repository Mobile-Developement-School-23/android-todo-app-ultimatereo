package com.example.android_todo_app_ultimatereo.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android_todo_app_ultimatereo.R
import com.example.android_todo_app_ultimatereo.data.TodoItem

class TodoAdapter(private val listener: Listener, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    private val differ: AsyncListDiffer<TodoItem> = AsyncListDiffer(this, DiffCallback())
    fun submitList(list: List<TodoItem>) = differ.submitList(list)
    fun currentList(): List<TodoItem> = differ.currentList

    interface Listener {
        fun onItemClicked(todoItem: TodoItem)
        fun onItemChecked(todoItem: TodoItem)
    }

    override fun onClick(v: View) {
        val itemPos = v.tag as Int
        val todoItem = currentList()[itemPos]
        when (v.id) {
            R.id.done -> listener.onItemChecked(todoItem)
            else -> listener.onItemClicked(todoItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val vh = TodoItemHolder(
            context,
            layoutInflater.inflate(
                R.layout.todo_item,
                parent,
                false
            )
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
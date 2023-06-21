package com.example.android_todo_app_ultimatereo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.android_todo_app_ultimatereo.recyclerview.TodoAdapter
import com.example.android_todo_app_ultimatereo.recyclerview.data.TodoItemsRepository

class MainActivity : AppCompatActivity() {
    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var doneTextView: TextView
    private val todoItemsRepository = TodoItemsRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoRecyclerView = findViewById(R.id.todo_items)
        val todoAdapter = TodoAdapter(this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        todoRecyclerView.adapter = todoAdapter
        todoRecyclerView.layoutManager = layoutManager
        todoAdapter.todoItems = todoItemsRepository.getTodoItems()
        todoRecyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))

        doneTextView = findViewById(R.id.done)
        doneTextView.text = "${todoItemsRepository.getTodoItems().count { td -> td.isDone }}"
//        TODO : Сделать так, чтобы счётчик обновлялся не только при обновлении страницы!



        val pullToRefresh: SwipeRefreshLayout = findViewById(R.id.pull_to_refresh)
        pullToRefresh.setOnRefreshListener {
            todoAdapter.todoItems = todoItemsRepository.getTodoItems()
            pullToRefresh.isRefreshing = false
            doneTextView.text = "${todoItemsRepository.getTodoItems().count { td -> td.isDone }}"
        }
    }
}
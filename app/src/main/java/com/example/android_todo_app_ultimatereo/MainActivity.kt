package com.example.android_todo_app_ultimatereo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.android_todo_app_ultimatereo.recyclerview.TodoAdapter
import com.example.android_todo_app_ultimatereo.recyclerview.data.TodoItemsRepository

class MainActivity : AppCompatActivity() {
    private lateinit var todoRecyclerView : RecyclerView
    private val todoItemsRepository = TodoItemsRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoRecyclerView = findViewById(R.id.todo_items)
        val todoAdapter = TodoAdapter()
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        todoRecyclerView.adapter = todoAdapter
        todoRecyclerView.layoutManager = layoutManager
        todoAdapter.todoItems = todoItemsRepository.getTodoItems()

        val pullToRefresh : SwipeRefreshLayout = findViewById(R.id.pull_to_refresh)
        pullToRefresh.setOnRefreshListener {
            todoAdapter.todoItems = todoItemsRepository.getTodoItems()
            pullToRefresh.isRefreshing = false
        }
    }
}
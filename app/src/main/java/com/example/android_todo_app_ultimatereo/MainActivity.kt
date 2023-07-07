package com.example.android_todo_app_ultimatereo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.android_todo_app_ultimatereo.recyclerview.TodoAdapter
import com.example.android_todo_app_ultimatereo.data.AppDatabase
import com.example.android_todo_app_ultimatereo.data.TodoDao
import com.example.android_todo_app_ultimatereo.data.TodoItemRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList

class MainActivity : AppCompatActivity() {
    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var doneTextView: TextView
    private lateinit var todoItemsRepository: TodoItemRepositoryImpl
    private lateinit var todoDao: TodoDao
    private companion object {
        const val DB_NAME = "todo.db"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoDao = Room.databaseBuilder(
            this.applicationContext,
            AppDatabase::class.java,
            DB_NAME).fallbackToDestructiveMigration().build().getTodoDao()

        todoRecyclerView = findViewById(R.id.todo_items)
        todoItemsRepository = TodoItemRepositoryImpl(todoDao)
        val todoAdapter = TodoAdapter(this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        todoRecyclerView.adapter = todoAdapter
        todoRecyclerView.layoutManager = layoutManager
        todoRecyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))

//        lifecycleScope.launch{
//
//        }
//        TODO :
        doneTextView = findViewById(R.id.done)
//        TODO : Сделать так, чтобы счётчик обновлялся не только при обновлении страницы!



        val pullToRefresh: SwipeRefreshLayout = findViewById(R.id.pull_to_refresh)
        pullToRefresh.setOnRefreshListener {
            pullToRefresh.isRefreshing = false
        }
    }
}
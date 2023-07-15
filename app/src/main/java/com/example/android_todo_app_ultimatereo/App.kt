package com.example.android_todo_app_ultimatereo

import android.app.Application
import androidx.room.Room
import com.example.android_todo_app_ultimatereo.data.AppDatabase
import com.example.android_todo_app_ultimatereo.data.TodoDao
import com.example.android_todo_app_ultimatereo.data.TodoItemRepositoryImpl
import com.example.android_todo_app_ultimatereo.data.TodoItemsRepository

class App : Application() {
    private lateinit var todoDao: TodoDao
    lateinit var repository: TodoItemsRepository

    override fun onCreate() {
        super.onCreate()
        todoDao = provideDao()
        repository = TodoItemRepositoryImpl(todoDao)
    }

    private fun provideDao(): TodoDao {
        return Room.databaseBuilder(
            this.applicationContext,
            AppDatabase::class.java,
            DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build().getTodoDao()
    }

    private companion object {
        const val DB_NAME = "todo.db"
    }
}
package com.example.android_todo_app_ultimatereo.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [
        TodoItem::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTodoDao(): TodoDao

}
package com.example.android_todo_app_ultimatereo.data

import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {
    suspend fun start()
    suspend fun addItem(item: TodoItem)

    suspend fun deleteItemById(id: String)

    suspend fun updateItem(item: TodoItem)

    suspend fun getTodoItemsFlowWith(isChecked: Boolean): Flow<List<TodoItem>>

    suspend fun getCountOfCompletedItems(): Int

    suspend fun getItemById(id: String): TodoItem?

    suspend fun getTodoItemsFlow(): Flow<List<TodoItem>>
}
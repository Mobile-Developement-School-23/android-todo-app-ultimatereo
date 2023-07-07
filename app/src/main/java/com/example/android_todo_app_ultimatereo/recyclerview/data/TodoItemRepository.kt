package com.example.android_todo_app_ultimatereo.recyclerview.data

import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {

    suspend fun addItem(item: TodoItem)

    suspend fun deleteItemById(id: String)

    suspend fun updateItem(item: TodoItem)

    suspend fun getTodoItemsFlowWith(isChecked: Boolean): Flow<List<TodoItem>>

    suspend fun getCountOfCompletedItems(): Int

    suspend fun getItemById(id: String): TodoItem?

    fun getTodoItemsFlow(): Flow<List<TodoItem>>
}
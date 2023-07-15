package com.example.android_todo_app_ultimatereo.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TodoItemRepositoryImpl(private val todoDao: TodoDao) : TodoItemsRepository {
    companion object {
        private val importancePriorities = TodoPriority.values()
    }

    override suspend fun start() {
        withContext(Dispatchers.IO) {
            todoDao.deleteAll()
            todoDao.save(generateList())
        }
    }

    private fun generateList(): MutableList<TodoItem> {
        return buildList {
            val numberOfItems = 30
            for (i in 0 until numberOfItems) {
                val id = "TodoItem_$i"
                val text: String = id
                val importance: TodoPriority = importancePriorities.random()
                val isDone: Boolean = booleanArrayOf(true, false).random()
                val deadline = System.currentTimeMillis() + (1..1000).random()
                val timeOfCreation = System.currentTimeMillis() - (1..1000).random()
                val timeOfLastChange =System.currentTimeMillis() - (1..100).random()
                val todoItem = TodoItem(
                    id,
                    text,
                    importance,
                    deadline,
                    isDone,
                    timeOfCreation,
                    timeOfLastChange
                )
                add(todoItem)
            }
        }.toMutableList()
    }

    override suspend fun addItem(item: TodoItem) {
        withContext(Dispatchers.IO) {
            todoDao.save(item)
        }
    }

    override suspend fun deleteItemById(id: String) {
        withContext(Dispatchers.IO) {
            todoDao.deleteById(id)
        }
    }

    override suspend fun updateItem(item: TodoItem) {
        val itemToUpdate = todoDao.getById(item.id)

        itemToUpdate?.let {
            val updatedItem = item.copy(timeOfCreation = System.currentTimeMillis())
            todoDao.update(updatedItem)
        }
        return
    }

    override suspend fun getTodoItemsFlowWith(isChecked: Boolean): Flow<List<TodoItem>> {
        return withContext(Dispatchers.IO) {
            if (isChecked) {
                return@withContext todoDao.getAllFlowWithCheckedState(isChecked = false)
            }
            return@withContext todoDao.getAllFlow()
        }
    }

    override suspend fun getCountOfCompletedItems(): Int {
        return withContext(Dispatchers.IO) {
            return@withContext todoDao.getCompletedCount()
        }
    }

    override suspend fun getItemById(id: String): TodoItem? {
        return withContext(Dispatchers.IO) {
            return@withContext todoDao.getById(id)
        }
    }

    override suspend fun getTodoItemsFlow(): Flow<List<TodoItem>> {
        return withContext(Dispatchers.IO) {
            return@withContext todoDao.getAllFlow()
        }
    }
}
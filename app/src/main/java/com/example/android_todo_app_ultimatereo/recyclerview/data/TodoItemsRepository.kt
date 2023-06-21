package com.example.android_todo_app_ultimatereo.recyclerview.data

import java.sql.Timestamp

class TodoItemsRepository {
    companion object {
        private const val upperBoundaryLength = 100
        private val importancePriorities = TodoPriority.values()
    }

    private val todoItems: MutableList<TodoItem>
    private var countOfDone: Int

    init {
        todoItems = generateList()
        countOfDone = todoItems.count { it.isDone }
    }

    fun getTodoItems(): List<TodoItem> {
        return todoItems
    }

    private fun generateList(): MutableList<TodoItem> {
        return buildList {
            val numberOfItems = (10..30).random()
            for (i in 0 until numberOfItems) {
                val id = "TodoItem_$i"
                val text: String = getRandomString((1..upperBoundaryLength).random())
                val importance: TodoPriority = importancePriorities.random()
                val isDone: Boolean = booleanArrayOf(true, false).random()
                val deadline = Timestamp(System.currentTimeMillis() + (1..1000).random())
                val timeOfCreation = Timestamp(System.currentTimeMillis() - (1..1000).random())
                val timeOfLastChange = Timestamp(System.currentTimeMillis() - (1..100).random())
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

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}
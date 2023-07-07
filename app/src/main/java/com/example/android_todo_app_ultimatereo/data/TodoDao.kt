package com.example.android_todo_app_ultimatereo.data

import kotlinx.coroutines.flow.Flow
import androidx.room.*
@Dao
interface TodoDao {

    @Query("SELECT * FROM todoItems")
    fun getAllFlow(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todoItems WHERE isDone = :isChecked")
    fun getAllFlowWithCheckedState(isChecked: Boolean): Flow<List<TodoItem>>

    @Query("SELECT * FROM todoItems WHERE id = :itemId")
    suspend fun getById(itemId: String): TodoItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(item: TodoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(items: List<TodoItem>)

    @Update
    suspend fun update(item: TodoItem)

    @Delete
    suspend fun delete(item: TodoItem)

    @Query("DELETE FROM todoItems WHERE id = :itemId")
    suspend fun deleteById(itemId: String)

    @Query("DELETE FROM todoItems")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM todoItems WHERE isDone = 1")
    fun getCompletedCount(): Int

}
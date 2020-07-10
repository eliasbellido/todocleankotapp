package com.beyondthecode.todocleankotapp.data.source.local

import androidx.room.*
import com.beyondthecode.todocleankotapp.data.Task

/**
 * Created by Elías Bellido on 7/7/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */

/**
* Data Access Object for the tasks table.
*/
@Dao
interface TasksDao {

    @Query("SELECT * FROM Tasks")
    suspend fun getTasks(): List<Task>

    @Query("SELECT * FROM Tasks WHERE entryId = :taskId")
    suspend fun getTaskById(taskId: String): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task): Int

    @Query("UPDATE tasks SET completed = :completed WHERE entryid = :taskId")
    suspend fun updateCompleted(taskId: String, completed: Boolean)

    @Query("DELETE FROM Tasks WHERE entryId = :taskId")
    suspend fun deleteTaskById(taskId: String): Int

    @Query("DELETE FROM Tasks")
    suspend fun deleteTasks()

    @Query("DELETE FROM Tasks WHERE completed = 1")
    suspend fun deleteCompletedTasks(): Int

}
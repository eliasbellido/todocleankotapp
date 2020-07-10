package com.beyondthecode.todocleankotapp.data.source

import com.beyondthecode.todocleankotapp.data.Result
import com.beyondthecode.todocleankotapp.data.Task

/**
 * Created by Elías Bellido on 7/7/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */

/**
 * Main entry point for accessing tasks data.
 */
interface TasksDataSource {

    suspend fun getTasks(): Result<List<Task>>

    suspend fun getTask(taskId: String): Result<Task>

    suspend fun saveTask(task: Task)

    suspend fun completeTask(task: Task)

    suspend fun completeTask(task: String)

    suspend fun activateTask(task: Task)

    suspend fun activateTask(taskId: String)

    suspend fun clearCompletedTasks()

    suspend fun deleteAllTasks()

    suspend fun deleteTask(taskId: String)

}
package com.beyondthecode.todocleankotapp.data.source.local

import com.beyondthecode.todocleankotapp.data.Result
import com.beyondthecode.todocleankotapp.data.Task
import com.beyondthecode.todocleankotapp.data.source.TasksDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Elías Bellido on 7/7/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */

/**
 * Concrete implementation of a data source as a db.
 */
class TasksLocalDataSource internal constructor(
    private val tasksDao: TasksDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksDataSource {

    override suspend fun getTasks(): Result<List<Task>> = withContext(ioDispatcher){
            return@withContext try {
                Result.Success(tasksDao.getTasks())
            }catch (e: Exception){
                Result.Error(e)
            }
    }

    override suspend fun getTask(taskId: String): Result<Task> = withContext(ioDispatcher){
        try{
            val task = tasksDao.getTaskById(taskId)
            task?.let{
                return@withContext Result.Success(it)
            } ?: run {
                return@withContext Result.Error(Exception("task not fund!"))
            }
        }catch (e: Exception){
            return@withContext Result.Error(e)
        }
    }

    override suspend fun saveTask(task: Task) {
        tasksDao.insertTask(task)
    }

    override suspend fun completeTask(task: Task) {
        tasksDao.updateCompleted(task.id, true)
    }

    override suspend fun completeTask(taskId: String) {
        tasksDao.updateCompleted(taskId, true)
    }

    override suspend fun activateTask(task: Task) = withContext(ioDispatcher){
        tasksDao.updateCompleted(task.id, false)
    }


    override suspend fun activateTask(taskId: String) {
        tasksDao.updateCompleted(taskId, false)
    }

    override suspend fun clearCompletedTasks() = withContext<Unit>(ioDispatcher){
        tasksDao.deleteCompletedTasks()
    }

    override suspend fun deleteAllTasks() = withContext(ioDispatcher){
        tasksDao.deleteTasks()
    }

    override suspend fun deleteTask(taskId: String) = withContext<Unit>(ioDispatcher){
        tasksDao.deleteTaskById(taskId)
    }


}
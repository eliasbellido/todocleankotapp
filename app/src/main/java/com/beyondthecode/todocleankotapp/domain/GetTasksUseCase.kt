package com.beyondthecode.todocleankotapp.domain

import com.beyondthecode.todocleankotapp.data.Result
import com.beyondthecode.todocleankotapp.data.Task
import com.beyondthecode.todocleankotapp.data.source.TasksRepository
import com.beyondthecode.todocleankotapp.presentation.tasks.TasksFilterType
import com.beyondthecode.todocleankotapp.presentation.tasks.TasksFilterType.*

/**
 * Created by Elías Bellido on 7/8/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */
class GetTasksUseCase (
    private val tasksRepository: TasksRepository
) {
    suspend operator fun invoke(
        forceUpdate: Boolean = false,
        currentFiltering: TasksFilterType = ALL_TASKS
    ): Result<List<Task>> {

        val tasksResult = tasksRepository.getTasks(forceUpdate)

        //Filter tasks
        if(tasksResult is Result.Success && currentFiltering != ALL_TASKS){
            val tasks = tasksResult.data

            val tasksToShow = mutableListOf<Task>()
            //We filter the tasks based on the requestType
            for(task in tasks){
                when(currentFiltering){
                    ACTIVE_TASKS -> if(task.isActive){
                        tasksToShow.add(task)
                    }
                    COMPLETED_TASKS -> if(task.isCompleted){
                        tasksToShow.add(task)
                    }
                    else -> NotImplementedError()
                }
            }
            return Result.Success(tasksToShow)
        }
        return tasksResult
    }
}
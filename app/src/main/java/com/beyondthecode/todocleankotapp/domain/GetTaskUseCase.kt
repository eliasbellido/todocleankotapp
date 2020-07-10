package com.beyondthecode.todocleankotapp.domain

import com.beyondthecode.todocleankotapp.data.Result
import com.beyondthecode.todocleankotapp.data.Task
import com.beyondthecode.todocleankotapp.data.source.TasksRepository

/**
 * Created by Elías Bellido on 7/8/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */
class GetTaskUseCase (
    private val tasksRepository: TasksRepository
) {
    suspend operator fun invoke(taskId: String, forceUpdate: Boolean = false): Result<Task> {
        return tasksRepository.getTask(taskId, forceUpdate)
    }
}
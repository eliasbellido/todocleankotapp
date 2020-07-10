package com.beyondthecode.todocleankotapp.domain

import com.beyondthecode.todocleankotapp.data.source.TasksRepository

/**
 * Created by Elías Bellido on 7/8/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */
class DeleteTaskUseCase (
    private val tasksRepository: TasksRepository
) {
    suspend operator fun invoke(taskId: String) {
        return tasksRepository.deleteTask(taskId)
    }
}
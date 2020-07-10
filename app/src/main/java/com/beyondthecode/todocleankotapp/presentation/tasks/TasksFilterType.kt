package com.beyondthecode.todocleankotapp.presentation.tasks

/**
 * Created by Elías Bellido on 7/8/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */

/**
 * Used with the filter spinner in the tasks list.
 */
enum class TasksFilterType {
    /**
     * Do not filter tasks.
     */
    ALL_TASKS,

    /**
     * Filters only the active (not completed yet) tasks.
     */
    ACTIVE_TASKS,

    /**
     * Filters only the completed tasks.
     */
    COMPLETED_TASKS
}
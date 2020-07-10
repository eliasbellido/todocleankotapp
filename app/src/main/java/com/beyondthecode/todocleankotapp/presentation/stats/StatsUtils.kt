package com.beyondthecode.todocleankotapp.presentation.stats

import com.beyondthecode.todocleankotapp.data.Task

/**
 * Created by Elías Bellido on 7/8/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */

/**
 * Function that does some trivial computation. Used to showcase unit tests.
 */
internal fun getActiveAndCompletedStats(tasks: List<Task>?): StatsResult{
    return if(tasks == null ||tasks.isEmpty()){
        StatsResult(0f, 0f)
    }else{
        val totalTasks = tasks.size
        val numberOfActiveTasks = tasks.count{ it.isActive }
        StatsResult(
            activeTasksPercent = 100f * numberOfActiveTasks / tasks.size,
            completedTasksPercent = 100f * (totalTasks - numberOfActiveTasks) / tasks.size
        )
    }
}

data class StatsResult(val activeTasksPercent: Float, val completedTasksPercent: Float)
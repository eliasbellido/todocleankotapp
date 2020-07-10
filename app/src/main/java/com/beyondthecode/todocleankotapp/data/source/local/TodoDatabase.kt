package com.beyondthecode.todocleankotapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.beyondthecode.todocleankotapp.data.Task

/**
 * Created by Elías Bellido on 7/7/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase(){

    abstract fun taskDao(): TasksDao
}
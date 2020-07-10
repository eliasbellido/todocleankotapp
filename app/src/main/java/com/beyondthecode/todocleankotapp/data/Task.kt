package com.beyondthecode.todocleankotapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by Elías Bellido on 7/7/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */

@Entity(tableName = "tasks")
data class Task @JvmOverloads constructor(
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "completed") var isCompleted: Boolean = false,
    @PrimaryKey @ColumnInfo(name = "entryId") var id: String = UUID.randomUUID().toString()
){

    val titleForList : String = if (title.isNotEmpty()) title else description

    val isActive = !isCompleted

    val isEmpty = title.isEmpty() ||description.isNotEmpty()
}

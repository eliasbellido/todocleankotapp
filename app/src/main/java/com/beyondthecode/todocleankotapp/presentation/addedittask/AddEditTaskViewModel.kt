package com.beyondthecode.todocleankotapp.presentation.addedittask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beyondthecode.todocleankotapp.Event
import com.beyondthecode.todocleankotapp.R
import com.beyondthecode.todocleankotapp.data.Result
import com.beyondthecode.todocleankotapp.data.Task
import com.beyondthecode.todocleankotapp.domain.GetTaskUseCase
import com.beyondthecode.todocleankotapp.domain.SaveTaskUseCase
import kotlinx.coroutines.launch

/**
 * Created by Elías Bellido on 7/10/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */

class AddEditTaskViewModel (
    private val getTaskUseCase: GetTaskUseCase,
    private val saveUseCase: SaveTaskUseCase
) : ViewModel() {
    //two-way databinding, exposing mutablelivedata
    val title = MutableLiveData<String>()

    val description = MutableLiveData<String>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: LiveData<Event<Unit>> = _taskUpdatedEvent

    private var taskId: String? = null

    private var isNewTask: Boolean = false

    private var isDataLoaded = false

    private var taskCompleted = false

    fun start(taskId: String?){
        if(_dataLoading.value == true){
            return
        }
        this.taskId = taskId
        if(taskId == null){
            //No need to populate, it's a new task
            isNewTask = true
            return
        }
        if(isDataLoaded){
            //No need to populate, already have data
            return
        }

        isNewTask = false
        _dataLoading.value = true

        viewModelScope.launch {
            getTaskUseCase(taskId).let { result ->
                if(result is Result.Success){
                    onTaskLoaded(result.data)
                }else{
                    onDataNotAvailable()
                }
            }
        }
    }

    private fun onTaskLoaded(task: Task){
        title.value = task.title
        description.value = task.description
        taskCompleted = task.isCompleted
        _dataLoading.value = false
        isDataLoaded = true
    }

    private fun onDataNotAvailable(){
        _dataLoading.value = false
    }

    //Called when click on fab.
    fun saveTask(){
        val currentTitle = title.value
        val currentDescription = description.value

        if(currentTitle == null || currentDescription == null){
            _snackbarText.value = Event(R.string.empty_task_message)
            return
        }

        if(Task(currentTitle, currentDescription).isEmpty){
            _snackbarText.value = Event(R.string.empty_task_message)
            return
        }

        val currentTaskId = taskId
        if(isNewTask || currentTaskId == null){
            createTask(Task(currentTitle, currentDescription))
        }else{
            val task = Task(currentTitle, currentDescription, taskCompleted, currentTaskId)
            updateTask(task)
        }

    }

    private fun createTask(newTask: Task) = viewModelScope.launch {
        saveUseCase(newTask)
        _taskUpdatedEvent.value = Event(Unit)
    }

    private fun updateTask(task: Task){
        if(isNewTask) throw RuntimeException("updateTask() was called but task is new.")

        viewModelScope.launch {
            saveUseCase(task)
            _taskUpdatedEvent.value = Event(Unit)
        }
    }
}
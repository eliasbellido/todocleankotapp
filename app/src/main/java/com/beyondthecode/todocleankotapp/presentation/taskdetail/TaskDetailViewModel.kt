package com.beyondthecode.todocleankotapp.presentation.taskdetail

import com.beyondthecode.todocleankotapp.R
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.beyondthecode.todocleankotapp.Event
import com.beyondthecode.todocleankotapp.data.Result
import com.beyondthecode.todocleankotapp.data.Task
import com.beyondthecode.todocleankotapp.domain.ActivateTasksUseCase
import com.beyondthecode.todocleankotapp.domain.CompleteTasksUseCase
import com.beyondthecode.todocleankotapp.domain.DeleteTaskUseCase
import com.beyondthecode.todocleankotapp.domain.GetTaskUseCase
import kotlinx.coroutines.launch

/**
 * Created by Elías Bellido on 7/10/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */
class TaskDetailViewModel (
    private val getTaskUseCase: GetTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val completeTasksUseCase: CompleteTasksUseCase,
    private val activateTasksUseCase: ActivateTasksUseCase
) : ViewModel() {

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task> = _task

    private val _isDataAvailable = MutableLiveData<Boolean>()
    val isDataAvailable: LiveData<Boolean> = _isDataAvailable

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _editTaskEvent = MutableLiveData<Event<Unit>>()
    val editTaskEvent: LiveData<Event<Unit>> = _editTaskEvent

    private val _deleteTaskEvent = MutableLiveData<Event<Unit>>()
    val deleteTaskEvent: LiveData<Event<Unit>> = _deleteTaskEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val taskId: String?
        get() = _task.value?.id

    //This LiveData depends on another so we can use a transformation.
    val completed: LiveData<Boolean> = Transformations.map(_task){ input: Task? ->
        input?.isCompleted ?: false
    }

    fun deleteTask() = viewModelScope.launch {
        taskId?.let{
            deleteTaskUseCase(it)
            _deleteTaskEvent.value = Event(Unit)
        }
    }

    fun editTask(){
        _editTaskEvent.value = Event(Unit)
    }

    fun setCompleted(completed: Boolean) = viewModelScope.launch {
        val task = _task.value ?: return@launch
        if(completed){
            completeTasksUseCase(task)
            showSnackbarMessage(R.string.task_marked_complete)
        }else{
            activateTasksUseCase(task)
            showSnackbarMessage(R.string.task_marked_active)
        }
    }

    fun start(taskId: String?, forceRefresh: Boolean = false){
        if(_isDataAvailable.value == true && !forceRefresh ||_dataLoading.value == true){
            return
        }

        //show loading indicator
        _dataLoading.value = true

        viewModelScope.launch {
            taskId?.let {
                getTaskUseCase(taskId, false).let { result ->
                    if(result is Result.Success){
                        onTaskLoaded(result.data)
                    }else{
                        onDataNotAvailable(result)
                    }

                }
            }
            _dataLoading.value = false
        }
    }

    private fun onTaskLoaded(task: Task){
        setTask(task)
    }

    private fun onDataNotAvailable(result: Result<Task>){
        _task.value = null
        _isDataAvailable.value = false
    }

    fun refresh(){
        taskId?.let{ start(it, true) }
    }
    private fun setTask(task: Task?){
        this._task.value = task
        _isDataAvailable.value = task != null
    }

    private fun showSnackbarMessage(@StringRes message: Int){
        _snackbarText.value = Event(message)
    }
}
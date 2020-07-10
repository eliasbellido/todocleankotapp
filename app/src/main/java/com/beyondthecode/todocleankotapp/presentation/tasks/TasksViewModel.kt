package com.beyondthecode.todocleankotapp.presentation.tasks

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.beyondthecode.todocleankotapp.Event
import com.beyondthecode.todocleankotapp.R
import com.beyondthecode.todocleankotapp.data.Result
import com.beyondthecode.todocleankotapp.data.Task
import com.beyondthecode.todocleankotapp.domain.ActivateTasksUseCase
import com.beyondthecode.todocleankotapp.domain.ClearCompletedTasksUseCase
import com.beyondthecode.todocleankotapp.domain.CompleteTasksUseCase
import com.beyondthecode.todocleankotapp.domain.GetTasksUseCase
import kotlinx.coroutines.launch

/**
 * Created by Elías Bellido on 7/8/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */

class TasksViewModel (
    private val getTasksUseCase: GetTasksUseCase,
    private val clearCompletedTasksUseCase: ClearCompletedTasksUseCase,
    private val completeTaskUseCase: CompleteTasksUseCase,
    private val activateTaskUseCase: ActivateTasksUseCase
) : ViewModel() {

    private val _items = MutableLiveData<List<Task>>().apply { value = emptyList() }
    val items: LiveData<List<Task>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _currentFilteringLabel = MutableLiveData<Int>()
    val currentFilteringLabel: LiveData<Int> = _currentFilteringLabel

    private val _noTasksLabel = MutableLiveData<Int>()
    val noTasksLabel: LiveData<Int> = _noTasksLabel

    private val _noTaskIconRes = MutableLiveData<Int>()
    val noTaskIconRes: LiveData<Int> = _noTaskIconRes

    private val _tasksAddViewVisible = MutableLiveData<Boolean>()
    val tasksAddViewVisible: LiveData<Boolean> = _tasksAddViewVisible

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private var _currentFiltering = TasksFilterType.ALL_TASKS

    //Not used at the moment
    private val isDataLoadingError = MutableLiveData<Boolean>()

    private val _openTaskEvent = MutableLiveData<Event<String>>()
    val openTaskEvent: LiveData<Event<String>> = _openTaskEvent

    private val _newTaskEvent = MutableLiveData<Event<Unit>>()
    val newTaskEvent: LiveData<Event<Unit>> = _newTaskEvent

    //This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    init {
        //Set initial state
        setFiltering(TasksFilterType.ALL_TASKS)
        loadTasks(true)
    }

    fun setFiltering(requestType: TasksFilterType){
        _currentFiltering = requestType

        //Depending on the filter type, set the filtering label, icon drawables, etc.
        when(requestType){
            TasksFilterType.ALL_TASKS -> {
                setFilter(
                    R.string.label_all, R.string.no_tasks_all,
                    R.drawable.logo_no_fill, true
                )
            }
            TasksFilterType.ACTIVE_TASKS -> {
                setFilter(
                    R.string.label_active, R.string.no_tasks_active,
                    R.drawable.ic_check_circle_96dp, false
                )
            }
            TasksFilterType.COMPLETED_TASKS -> {
                setFilter(
                    R.string.label_completed, R.string.no_tasks_completed,
                    R.drawable.ic_verified_user_96dp, false
                )
            }
        }
    }

    private fun setFilter(
        @StringRes filteringLabelString: Int, @StringRes noTasksLabelString: Int,
        @DrawableRes noTaskIconDrawable: Int, tasksAddVisible: Boolean
    ){
        _currentFilteringLabel.value = filteringLabelString
        _noTasksLabel.value = noTasksLabelString
        _noTaskIconRes.value = noTaskIconDrawable
        _tasksAddViewVisible.value = tasksAddVisible
    }

    fun clearCompletedTasks(){
        viewModelScope.launch {
            clearCompletedTasksUseCase()
            showSnackbarMessage(R.string.completed_tasks_cleared)
            //refresh list to show the new state
            loadTasks(false)

        }
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        if(completed){
            completeTaskUseCase(task) //TODO: add parameter
            showSnackbarMessage(R.string.task_marked_complete)
        }else{
            activateTaskUseCase(task)
            showSnackbarMessage(R.string.task_marked_active)
        }
        //Refresh list to show the new state
        loadTasks(false)
    }

    fun addNewTask(){
        _newTaskEvent.value = Event(Unit)
    }

    fun openTask(taskId: String){
        _openTaskEvent.value = Event(taskId)
    }

    fun showEditResultMessage(result: Int){
        when(result){
            EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_saved_task_message)
            ADD_EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_added_task_message)
            DELETE_RESULT_OK -> showSnackbarMessage(R.string.successfully_deleted_task_message)
        }
    }

    private fun showSnackbarMessage(message: Int){
        _snackbarText.value = Event(message)
    }

    fun loadTasks(forceUpdate: Boolean){
        _dataLoading.value = true

        viewModelScope.launch {
            val tasksResult = getTasksUseCase(forceUpdate, _currentFiltering)
            if(tasksResult is Result.Success){
                isDataLoadingError.value = false
                _items.value = tasksResult.data
            }else{
                isDataLoadingError.value = false
                _items.value = emptyList()
                showSnackbarMessage(R.string.loading_tasks_error)
            }

            _dataLoading.value = false
        }
    }

    fun refresh(){
        loadTasks(true)
    }
}
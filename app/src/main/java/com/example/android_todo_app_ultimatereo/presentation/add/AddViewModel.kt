package com.example.android_todo_app_ultimatereo.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.android_todo_app_ultimatereo.App
import com.example.android_todo_app_ultimatereo.data.TodoItem
import com.example.android_todo_app_ultimatereo.data.TodoItemsRepository
import com.example.android_todo_app_ultimatereo.data.TodoPriority
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID


class AddViewModel(repository: TodoItemsRepository) : ViewModel() {
    sealed class EventUi {
        object OnCancelButtonClicked : EventUi()
        object OnSaveButtonClicked : EventUi()
        object OnDeleteButtonClicked : EventUi()
        object OnBackButtonClicked : EventUi()
        data class OnTodoTextEdited(val editedText: String) : EventUi()
        data class OnImportanceSelected(val priority: TodoPriority) : EventUi()
        data class OnDeadlineSelected(val timestamp: Long) : EventUi()
        data class OnDeadlineSwitchChanged(val isChecked: Boolean) : EventUi()
        data class OnTodoItemIdLoaded(val todoItemId: String) : EventUi()
    }

    sealed class FragmentViewState {
        object Loading : FragmentViewState()
        object Update : FragmentViewState()
    }

    sealed class EffectUi {
        data class ShowSnackbar(val message: String) : EffectUi()
        object ToBackFragment : EffectUi()
        object ShowDatePicker : EffectUi()
    }

    data class UiState(
        var id: String? = null,
        var text: String? = null,
        var priority: TodoPriority = TodoPriority.LOW,
        var deadline: Long? = null,
        var isDone: Boolean = false,
        var timeOfCreation: Long? = null,
        val viewState: FragmentViewState = FragmentViewState.Loading
    )

    private val _event: MutableSharedFlow<EventUi> = MutableSharedFlow()

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _effect: Channel<EffectUi> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setState(reduce: UiState.() -> UiState) {
        val newState = uiState.value.reduce()
        _uiState.value = newState
    }

    fun setEvent(event: EventUi) = viewModelScope.launch { _event.emit(event) }

    private fun setEffect(builder: () -> EffectUi) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    init {
        viewModelScope.launch {
            _event.collect { event ->
                when (event) {
                    is EventUi.OnBackButtonClicked -> setEffect { EffectUi.ToBackFragment }
                    is EventUi.OnCancelButtonClicked -> setEffect { EffectUi.ToBackFragment }
                    is EventUi.OnSaveButtonClicked -> {

                        val newItem = validateSaveTask() ?: return@collect

                        if (uiState.value.id == null) {
                            repository.addItem(newItem)
                        } else {
                            repository.updateItem(newItem)
                        }
                        setEffect { EffectUi.ToBackFragment }
                    }

                    is EventUi.OnDeleteButtonClicked -> {
                        val currentTodoItem = uiState.value
                        currentTodoItem.id?.let {
                            repository.deleteItemById(it)
                        }
                        setEffect { EffectUi.ToBackFragment }
                    }

                    is EventUi.OnTodoTextEdited -> {
                        setState {
                            copy(
                                text = event.editedText,
                                viewState = FragmentViewState.Update
                            )
                        }
                    }

                    is EventUi.OnImportanceSelected -> {
                        setState {
                            copy(priority = event.priority)
                        }
                    }

                    is EventUi.OnDeadlineSelected -> {
                        setState {
                            copy(deadline = event.timestamp)
                        }
                    }

                    is EventUi.OnDeadlineSwitchChanged -> {
                        if (event.isChecked) {
                            setEffect { EffectUi.ShowDatePicker }
                            return@collect
                        }
                        setState {
                            copy(deadline = null)
                        }
                    }

                    is EventUi.OnTodoItemIdLoaded -> {
                        val todoItem = repository.getItemById(event.todoItemId) ?: return@collect
                        setState {
                            copy(
                                id = todoItem.id,
                                text = todoItem.text,
                                priority = todoItem.priority,
                                deadline = todoItem.deadline,
                                isDone = todoItem.isDone,
                                timeOfCreation = todoItem.timeOfCreation
                            )
                        }
                    }
                }
            }
        }
    }

    private fun validateSaveTask(): TodoItem? {
        val currentTime = System.currentTimeMillis() / 1000
        val currentTodoItem = uiState.value
        if (currentTodoItem.text.isNullOrEmpty()) {
            setEffect { EffectUi.ShowSnackbar("Необходимо ввести описание дела") }
            return null
        }
        return TodoItem(
            id = currentTodoItem.id ?: UUID.randomUUID().toString(),
            text = currentTodoItem.text ?: "",
            priority = currentTodoItem.priority,
            deadline = currentTodoItem.deadline,
            isDone = currentTodoItem.isDone,
            timeOfCreation = currentTodoItem.timeOfCreation ?: currentTime,
            timeOfLastChange = currentTime
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as App).repository
                AddViewModel(
                    repository = repository,
                )
            }
        }
    }
}
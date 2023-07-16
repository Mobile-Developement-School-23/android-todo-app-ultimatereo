package com.example.android_todo_app_ultimatereo.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.android_todo_app_ultimatereo.App
import com.example.android_todo_app_ultimatereo.data.TodoItem
import com.example.android_todo_app_ultimatereo.data.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(repository: TodoItemsRepository) :
    ViewModel() {
    sealed class EventUi {
        data class OnVisibleChange(val isFilterCompleted: Boolean) : EventUi()
        data class OnItemSelected(val todoItem: TodoItem) : EventUi()
        data class OnItemCheckedChange(val todoItem: TodoItem) : EventUi()
        object OnFloatingButtonClick : EventUi()
    }

    sealed class EffectUi {
        data class ToAddFragmentUpdate(val todoItemId: String) : EffectUi()
        object ToAddFragmentCreate : EffectUi()
    }

    data class UiState(
        val countOfCompletedText: String = "",
        val todoItems: List<TodoItem> = listOf(),
        val isFilterCompleted: Boolean = false
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

    private val visibleStateFlow = MutableStateFlow(false)
    private val itemsFlow = visibleStateFlow.flatMapLatest {
        repository.getTodoItemsFlowWith(isChecked = it)
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.start()
        }
        viewModelScope.launch {
            itemsFlow.collect { list ->
                val count = repository.getCountOfCompletedItems()
                if (uiState.value.isFilterCompleted) {
                    setState {
                        copy(
                            countOfCompletedText = count.toString(),
                            todoItems = list.filter { !it.isDone }
                        )
                    }
                } else {
                    setState {
                        copy(
                            countOfCompletedText = count.toString(),
                            todoItems = list
                        )
                    }
                }
            }
        }
        viewModelScope.launch {
            _event.collect { event ->
                when (event) {
                    is EventUi.OnVisibleChange -> {
                        visibleStateFlow.value = event.isFilterCompleted
                        setState {
                            copy(
                                isFilterCompleted = event.isFilterCompleted
                            )
                        }
                    }

                    is EventUi.OnItemSelected -> {
                        val itemId = event.todoItem.id
                        setEffect { EffectUi.ToAddFragmentUpdate(itemId) }
                    }

                    is EventUi.OnItemCheckedChange -> {
                        val itemChecked = event.todoItem
                            .copy(isDone = !event.todoItem.isDone)
                        repository.updateItem(itemChecked)
                    }

                    is EventUi.OnFloatingButtonClick -> {
                        setEffect { EffectUi.ToAddFragmentCreate }
                    }
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository =
                    (this[APPLICATION_KEY] as App).repository
                MainViewModel(
                    repository = repository,
                )
            }
        }
    }
}
package com.example.android_todo_app_ultimatereo.presentation.main

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.android_todo_app_ultimatereo.R
import com.example.android_todo_app_ultimatereo.data.TodoItem
import com.example.android_todo_app_ultimatereo.recyclerview.TodoAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.fragment_main) {
    private val vm: MainViewModel by viewModels { MainViewModel.Factory }
    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var doneTextView: TextView
    private lateinit var visibility: CheckBox
    private lateinit var add_item: FloatingActionButton
    private lateinit var pull_to_refresh: SwipeRefreshLayout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val root = requireView()
        todoRecyclerView = root.findViewById(R.id.todo_items)
        doneTextView = root.findViewById(R.id.done)
        visibility = root.findViewById(R.id.visibility)
        add_item = root.findViewById(R.id.add_item)
        pull_to_refresh = root.findViewById(R.id.pull_to_refresh)
        initVisibleButton()
        initRecyclerView()
        floatingButtonInit()
        initViewModelObservers()
    }

    private fun initViewModelObservers() {
        lifecycleScope.launch {
            vm.uiState.collectLatest { state ->
                doneTextView.text = state.countOfCompletedText
                todoAdapter.submitList(state.todoItems)
            }
        }
        lifecycleScope.launch {
            vm.effect.collect {
                when (it) {
                    is MainViewModel.EffectUi.ToAddFragmentUpdate -> {
                        val action = MainFragmentDirections.actionMainFragmentToAddFragment(
                            it.todoItemId
                        )
                        findNavController().navigate(action)
                    }

                    is MainViewModel.EffectUi.ToAddFragmentCreate -> {
                        val action = MainFragmentDirections.actionMainFragmentToAddFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun initVisibleButton() {
        visibility.setOnCheckedChangeListener { _, isChecked ->
            vm.setEvent(
                MainViewModel.EventUi.OnVisibleChange(isFilterCompleted = isChecked)
            )
        }
    }

    private fun initRecyclerView() {
        pull_to_refresh.setOnRefreshListener {
            pull_to_refresh.isRefreshing = false
        }
        todoAdapter = TodoAdapter(
            object : TodoAdapter.Listener {
                override fun onItemClicked(todoItem: TodoItem) {
                    vm.setEvent(
                        MainViewModel.EventUi.OnItemSelected(todoItem)
                    )
                }

                override fun onItemChecked(todoItem: TodoItem) {
                    vm.setEvent(
                        MainViewModel.EventUi.OnItemCheckedChange(todoItem)
                    )
                }
            },
            this
        )
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        todoRecyclerView.adapter = todoAdapter
        todoRecyclerView.layoutManager = layoutManager
        todoRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireActivity().applicationContext,
                layoutManager.orientation
            )
        )
    }

    private fun floatingButtonInit() {
        add_item.setOnClickListener {
            vm.setEvent(
                MainViewModel.EventUi.OnFloatingButtonClick
            )
        }
    }
}
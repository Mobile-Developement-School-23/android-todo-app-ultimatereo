package com.example.android_todo_app_ultimatereo.presentation.add

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android_todo_app_ultimatereo.R

class AddFragment : Fragment(R.layout.fragment_add_screen) {
    private val vm: AddViewModel by viewModels { AddViewModel.Factory }
}
package com.example.android_todo_app_ultimatereo.recyclerview

import android.content.Context
import android.graphics.Paint
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.android_todo_app_ultimatereo.R
import com.example.android_todo_app_ultimatereo.data.TodoItem
import com.example.android_todo_app_ultimatereo.presentation.main.MainFragment
import com.google.android.material.checkbox.MaterialCheckBox

class TodoItemHolder(private val fragment: MainFragment, itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    val root: View = itemView
    val todoText: TextView = itemView.findViewById(R.id.todo_text)
    val doneCheckBox: MaterialCheckBox = itemView.findViewById(R.id.done_todo_checkbox)
    var todoItem: TodoItem? = null

    fun onBind(todoItem: TodoItem) {
        this.todoItem = todoItem
        todoText.text = todoItem.text
        val item = todoItem
        doneCheckBox.isChecked = item.isDone
        if (item.isDone) {
            check()
        } else {
            uncheck()
        }
    }

    fun check() {
        todoText.setTextColor(resolveColorAttr(R.attr.label_tertiary))
        todoText.paintFlags =
            todoText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    fun uncheck() {
        todoText.setTextColor(resolveColorAttr(R.attr.label_primary))
        todoText.paintFlags =
            todoText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

    @ColorInt
    fun resolveColorAttr(@AttrRes colorAttr: Int): Int {
        val context = fragment.requireContext()
        val resolvedAttr = resolveThemeAttr(colorAttr, context)
        // resourceId is used if it's a ColorStateList, and data if it's a color reference or a hex color
        val colorRes =
            if (resolvedAttr.resourceId != 0) resolvedAttr.resourceId else resolvedAttr.data
        return ContextCompat.getColor(context, colorRes)
    }

    private fun resolveThemeAttr(@AttrRes attrRes: Int, context: Context): TypedValue {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrRes, typedValue, true)
        return typedValue
    }
}

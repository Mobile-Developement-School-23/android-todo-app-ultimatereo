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
import com.example.android_todo_app_ultimatereo.recyclerview.data.TodoItem
import com.google.android.material.checkbox.MaterialCheckBox

class TodoItemHolder(private val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val todoText: TextView = itemView.findViewById(R.id.todo_text)
    private val doneCheckBox: MaterialCheckBox = itemView.findViewById(R.id.done_todo_checkbox)
    private var firstTime = true
    fun onBind(todoItem: TodoItem) {
        todoText.text = todoItem.text
        doneCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                check(todoItem)
            } else {
                uncheck(todoItem)
            }
        }
        if (firstTime) {
            if (todoItem.isDone) {
                doneCheckBox.isChecked = todoItem.isDone
            }
            firstTime = false
        }
    }

    private fun check(todoItem: TodoItem) {
        todoText.setTextColor(context.resolveColorAttr(R.attr.label_tertiary))
        todoText.paintFlags =
            todoText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        todoItem.isDone = true
    }

    private fun uncheck(todoItem: TodoItem) {
        todoText.setTextColor(context.resolveColorAttr(R.attr.label_primary))
        todoText.paintFlags =
            todoText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        todoItem.isDone = false
    }

    @ColorInt
    fun Context.resolveColorAttr(@AttrRes colorAttr: Int): Int {
        val resolvedAttr = resolveThemeAttr(colorAttr)
        // resourceId is used if it's a ColorStateList, and data if it's a color reference or a hex color
        val colorRes =
            if (resolvedAttr.resourceId != 0) resolvedAttr.resourceId else resolvedAttr.data
        return ContextCompat.getColor(this, colorRes)
    }

    private fun Context.resolveThemeAttr(@AttrRes attrRes: Int): TypedValue {
        val typedValue = TypedValue()
        theme.resolveAttribute(attrRes, typedValue, true)
        return typedValue
    }
}

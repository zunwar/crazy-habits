package com.example.crazy_habits.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.crazy_habits.Priority
import com.example.crazy_habits.R


class CustomSpinnerAdapter(
    context: Context,
    textViewResourceId: Int,
    val list: Array<Priority>
    ) : ArrayAdapter<Priority>(
    context,
    textViewResourceId,
    list
    ) {
        override fun getCount() = list.size

        override fun getItem(position: Int) = list[position]

        override fun getItemId(position: Int) = list[position].id.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return (super.getDropDownView(position, convertView, parent) as TextView).apply {
                text = when (list[position]) {
                    Priority.High -> context.getString(R.string.highPriority)
                    Priority.Middle -> context.getString(R.string.middlePriority)
                    Priority.Low -> context.getString(R.string.lowPriority)
                }

            }
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return (super.getDropDownView(position, convertView, parent) as TextView).apply {
                text = when (list[position]) {
                    Priority.High -> context.getString(R.string.highPriority)
                    Priority.Middle -> context.getString(R.string.middlePriority)
                    Priority.Low -> context.getString(R.string.lowPriority)
                }
            }
        }
    }

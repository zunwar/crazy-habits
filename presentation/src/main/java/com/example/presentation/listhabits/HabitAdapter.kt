package com.example.presentation.listhabits

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.Habit
import com.example.domain.entities.Priority
import com.example.domain.entities.Type
import com.example.presentation.R
import com.example.presentation.databinding.ListItemViewBinding

class HabitAdapter(
    private val onItemClicked: (Habit) -> Unit,
    private val onItemLongClicked: (Habit) -> Unit
) : ListAdapter<Habit, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding =
            ListItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = HabitViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.absoluteAdapterPosition
            onItemClicked(getItem(position))
        }
        viewHolder.itemView.setOnLongClickListener {
            val position = viewHolder.absoluteAdapterPosition
            onItemLongClicked(getItem(position))
            true
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val habit = getItem(position)
        (holder as HabitViewHolder).bind(habit)
    }

    // DiffUtil takes care of the check of new list for changes
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Habit> =
            object : DiffUtil.ItemCallback<Habit>() {
                override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Habit, newItem: Habit
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    inner class HabitViewHolder(
        private val binding: ListItemViewBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val ctx = this.itemView.context

        fun bind(habit: Habit) {

            with(binding) {
                nameHabit.text = habit.name
                description.text = habit.desc
                type.text = when (habit.type) {
                    Type.Good -> ctx.getString(R.string.goodHabit)
                    Type.Bad -> ctx.getString(R.string.badHabit)
                }
                priority.text = when (habit.priority) {
                    Priority.Low -> ctx.getString(R.string.lowPriority)
                    Priority.Middle -> ctx.getString(R.string.middlePriority)
                    Priority.High -> ctx.getString(R.string.highPriority)
                }
                frequency.text = habit.frequency.toString()
                LLliv.background = GradientDrawable().apply { setColor(habit.colorHabit) }
            }
        }
    }

}
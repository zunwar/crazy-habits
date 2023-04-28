package com.example.crazy_habits.listhabits

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.crazy_habits.utils.Priority
import com.example.crazy_habits.R
import com.example.crazy_habits.utils.Type
import com.example.crazy_habits.database.habit.HabitEntity
import com.example.crazy_habits.databinding.ListItemViewBinding

class HabitAdapter (private val onItemClicked: (HabitEntity) -> Unit)
    : ListAdapter<HabitEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = ListItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = HabitViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.absoluteAdapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        val habit = getItem(position)
        (holder as HabitViewHolder).bind(habit)
    }

// DiffUtil takes care of the check of new list for changes
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<HabitEntity> = object : DiffUtil.ItemCallback<HabitEntity>() {
            override fun areItemsTheSame(oldItem: HabitEntity, newItem: HabitEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HabitEntity, newItem: HabitEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class HabitViewHolder(
        private val binding: ListItemViewBinding,
        ) : RecyclerView.ViewHolder(binding.root){

        private val ctx  = this.itemView.context

        fun bind (habit: HabitEntity) {

            with(binding) {
                nameHabit.text      = habit.name
                description.text    = habit.desc
                type.text           = when (habit.type){
                    Type.Good -> ctx.getString(R.string.goodHabit)
                    Type.Bad  -> ctx.getString(R.string.badHabit)
                }
                priority.text       =
                    when (habit.priority) {
                        Priority.Low    -> ctx.getString(R.string.lowPriority)
                        Priority.Middle -> ctx.getString(R.string.middlePriority)
                        Priority.High   -> ctx.getString(R.string.highPriority)
                    }
                frequency.text      = habit.frequency.toString()
                LLliv.background    = GradientDrawable().apply { setColor(habit.colorHabit) }
            }
        }
        }

}
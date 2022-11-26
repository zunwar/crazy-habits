package com.example.crazy_habits.adapters

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.crazy_habits.database.habit.Habit
import com.example.crazy_habits.Priority
import com.example.crazy_habits.R
import com.example.crazy_habits.Type
import com.example.crazy_habits.databinding.ListItemViewBinding

class HabitAdapter (private val itemClickListener: OnItemClickListener)
    : ListAdapter<Habit, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    private val habitList : MutableList<Habit> = mutableListOf()

//    override fun getItemCount(): Int {
//        return habitList.size
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = ListItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        val habit = getItem(position)
//        (holder as HabitViewHolder).bind(habitList[position])
        (holder as HabitViewHolder).bind(habit)
//        holder.bind(habitList[position])



    }

    fun addOrChangeHabit(newHabit: List<Habit>) {
        habitList.addAll(newHabit)
        submitList(newHabit) // DiffUtil takes care of the check of new list for changes
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Habit> = object : DiffUtil.ItemCallback<Habit>() {
            override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id : String)
    }

    inner class HabitViewHolder(
        private val binding: ListItemViewBinding,
        private val itemClickListener: OnItemClickListener
        ) : RecyclerView.ViewHolder(binding.root){

        val ctx  = this.itemView.context

        fun bind (habit: Habit) {

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
                period.text         = habit.period
                LLliv.background    = GradientDrawable().apply { setColor(habit.colorHabit) }
                LLliv.setOnClickListener {
                    itemClickListener.onItemClicked(id = habit.id)
                }
            }
        }
        }

}
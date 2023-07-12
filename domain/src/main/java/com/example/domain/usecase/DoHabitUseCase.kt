package com.example.domain.usecase

import com.example.domain.entities.Habit
import com.example.domain.entities.MessageDoHabit
import com.example.domain.entities.Type
import com.example.domain.repository.HabitListRepository
import javax.inject.Inject

class DoHabitUseCase @Inject constructor(
    private val habitListRepository: HabitListRepository
) {

    suspend operator fun invoke(habit: Habit): Pair<MessageDoHabit, Int> {
        val newHabit = habit.copy(doneCount = habit.doneCount + 1)
        val message: MessageDoHabit =
            if (newHabit.doneCount < habit.number) {
                if (newHabit.type == Type.Good) {
                    MessageDoHabit.DoGoodHabitMore
                } else {
                    MessageDoHabit.DoBadHabitMore
                }
            } else if (newHabit.type == Type.Good) {
                MessageDoHabit.DoGoodHabitEnough
            } else {
                MessageDoHabit.DoBadHabitEnough
            }

        habitListRepository.changeHabit(newHabit)
        val remainingDoCount = habit.number - newHabit.doneCount
        return Pair(message, remainingDoCount)
    }
}
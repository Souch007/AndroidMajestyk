package com.majestykapps.arch.presentation.common

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.majestykapps.arch.domain.repository.TasksRepository
import com.majestykapps.arch.domain.usecase.GetTask
import com.majestykapps.arch.domain.usecase.SubscribeTasks
import com.majestykapps.arch.presentation.taskdetail.TaskDetailViewModel
import com.majestykapps.arch.presentation.tasks.viewmodels.TasksViewModel

class ViewModelFactory private constructor(
    private val tasksRepository: TasksRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = with(modelClass) {
            if (modelClass.isAssignableFrom(TasksViewModel::class.java)){
                TasksViewModel(SubscribeTasks(tasksRepository))  as T
            } else if (modelClass.isAssignableFrom(TaskDetailViewModel::class.java)){
                TaskDetailViewModel(GetTask(tasksRepository))  as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
//        when {
//            isAssignableFrom(TasksViewModel::class.java) -> {
//                TasksViewModel(SubscribeTasks(tasksRepository))
//            }
//            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
//        }
    } as T

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(tasksRepository: TasksRepository) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(tasksRepository).also { INSTANCE = it }
            }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
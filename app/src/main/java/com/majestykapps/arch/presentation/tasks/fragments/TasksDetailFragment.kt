package com.majestykapps.arch.presentation.tasks.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.majestykapps.arch.R
import com.majestykapps.arch.data.api.TasksApiService
import com.majestykapps.arch.data.common.Resource
import com.majestykapps.arch.data.repository.TasksRepositoryImpl
import com.majestykapps.arch.data.source.local.TasksLocalDataSource
import com.majestykapps.arch.data.source.local.ToDoDatabase
import com.majestykapps.arch.domain.entity.Task
import com.majestykapps.arch.domain.usecase.GetTask
import com.majestykapps.arch.domain.usecase.GetTaskUseCase
import com.majestykapps.arch.presentation.common.ViewModelFactory
import com.majestykapps.arch.presentation.taskdetail.TaskDetailViewModel
import com.majestykapps.arch.presentation.tasks.viewmodels.TasksViewModel
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.fragment_tasks_detail.*

class TasksDetailFragment : Fragment(R.layout.fragment_tasks_detail) {
    private val viewModel: TasksViewModel by activityViewModels()
    private val viewModelDetail: TaskDetailViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            //perform your operation and call navigateUp
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModelObservers()
        change_source.setOnClickListener {
           TasksApiService.Config.BASE_URL = TasksApiService.Config.BASE_URL_2
           viewModelDetail.getTask(viewModel.launchEvent.value.toString())
        }

    }


    private fun initViewModelObservers() {
        viewModel.apply {
            tasks.observe(viewLifecycleOwner, Observer { tasks ->
                if(!tasks.isNullOrEmpty()) {
                   viewModelDetail.getTask(viewModel.launchEvent.value.toString())
                }
            })
        }

        viewModelDetail.apply {
            title.observe(viewLifecycleOwner, Observer { title ->
                tv_title.text = title.toString()
            })

            description.observe(viewLifecycleOwner, Observer { des ->
                tv_description.text = des.toString()
            })
        }
    }

    companion object {
        private const val TAG = "TasksDetailFragment"
        fun newInstance() = TasksDetailFragment()
    }

}
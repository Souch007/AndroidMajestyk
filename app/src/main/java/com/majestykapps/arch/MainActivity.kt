package com.majestykapps.arch

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.majestykapps.arch.data.repository.TasksRepositoryImpl
import com.majestykapps.arch.data.source.local.TasksLocalDataSource
import com.majestykapps.arch.data.source.local.ToDoDatabase
import com.majestykapps.arch.presentation.common.ViewModelFactory
import com.majestykapps.arch.presentation.taskdetail.TaskDetailViewModel
import com.majestykapps.arch.presentation.tasks.viewmodels.TasksViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var taskDetailViewModel: TaskDetailViewModel
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(dash_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        navController = findNavController(this, R.id.mainContent)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(this,navController,appBarConfiguration)
        tasksViewModel = initViewModel()
        taskDetailViewModel = initDetailViewModel()
        initViewModelObservers()
        if (savedInstanceState == null) {
            navController.navigate(R.id.tasksFragment)
        }

    }

    private fun initDetailViewModel(): TaskDetailViewModel {
        val factory = getFactory()
        return ViewModelProviders.of(this, factory).get(TaskDetailViewModel::class.java)
    }

    private fun initViewModel(): TasksViewModel {
        val factory = getFactory()
        return ViewModelProviders.of(this, factory).get(TasksViewModel::class.java)
    }

    private fun getFactory(): ViewModelProvider.Factory? {
        val tasksDao = ToDoDatabase.getInstance(applicationContext).taskDao()
        val localDataSource = TasksLocalDataSource.getInstance(tasksDao)
        val tasksRepository = TasksRepositoryImpl.getInstance(localDataSource)
        return ViewModelFactory.getInstance(tasksRepository)
    }

    private fun initViewModelObservers() {
        tasksViewModel.apply {
            launchEvent.observe(this@MainActivity, Observer { id ->
                Log.d(TAG, "launchTask: launching task with id = $id")
                // TODO add task detail fragment
            })
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

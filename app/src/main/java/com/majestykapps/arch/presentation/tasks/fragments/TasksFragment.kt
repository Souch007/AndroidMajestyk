package com.majestykapps.arch.presentation.tasks.fragments

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.majestykapps.arch.R
import com.majestykapps.arch.adapters.TaskListAdapter
import com.majestykapps.arch.domain.entity.Task
import com.majestykapps.arch.presentation.tasks.onTaskClickListner
import com.majestykapps.arch.presentation.tasks.viewmodels.TasksViewModel
import com.majestykapps.arch.util.SimpleDividerItemDecoration
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.fragment_tasks.*


class TasksFragment : Fragment(R.layout.fragment_tasks), onTaskClickListner , MaterialSearchBar.OnSearchActionListener {

    private val viewModel: TasksViewModel by activityViewModels()
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var allTasks: ArrayList<Task>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
        searchBar.setOnSearchActionListener(this)
    }

    fun isNetworkAvailable(activity: Activity): Boolean {
        var flag = false
        val cwjManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cwjManager.activeNetworkInfo != null) flag = cwjManager.activeNetworkInfo.isAvailable
        return flag
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModelObservers()
    }

    private fun initViewModelObservers() {

        viewModel.apply {
            loadingEvent.observe(viewLifecycleOwner, Observer { isRefreshing ->
                Log.d(TAG, "loadingEvent observed")
                swipeRefresh.isRefreshing = isRefreshing
            })

            errorEvent.observe(viewLifecycleOwner, Observer { throwable ->
                Log.e(TAG, "errorEvent observed", throwable)
                snackBarRetry()
                // TODO show error
            })

            tasks.observe(viewLifecycleOwner, Observer { tasks ->
                Log.d(TAG, "tasks observed: $tasks")
                //text.text = tasks.toString()
                allTasks = tasks as ArrayList<Task>;
                if (!allTasks.isNullOrEmpty()) {
                    text.visibility = View.GONE
                    populateTasks(allTasks);
                } else if (!isNetworkAvailable(requireActivity())) {
                    text.visibility = View.VISIBLE
                    text.text = "No Internet Connection Available,\n Please Reconnect and Try Again"
                } else {
                    text.visibility = View.VISIBLE
                    text.text = "No Task Found"
                }
            })
        }
    }

    private fun snackBarRetry() {
        Snackbar.make(swipeRefresh, "Error, No Data To Load ,", Snackbar.LENGTH_LONG)
            .setAction("Retry") {
                viewModel.refresh()
            }
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
            .show()
    }

    private fun populateTasks(tasks: ArrayList<Task>) {
        val taskListAdapter = TaskListAdapter(requireActivity(), tasks, this)
        rv_tasks_list.adapter = taskListAdapter
        rv_tasks_list.layoutManager = linearLayoutManager
        rv_tasks_list.addItemDecoration(
            SimpleDividerItemDecoration(
                requireContext(),
                R.drawable.line
            )
        );
    }


    companion object {
        private const val TAG = "TasksFragment"

        fun newInstance() = TasksFragment()
    }

    override fun onTaskItemClickListner(id: String) {
        super.onTaskItemClickListner(id)
        viewModel.onTaskClick(id);
        findNavController(requireView()).navigate(R.id.tasksDetailFragment)
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        if(!enabled){
            populateTasks(allTasks)
        }
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        if(!text.toString().isNullOrEmpty()){
            var tasks: ArrayList<Task>? = ArrayList()
            for (i in 0 until allTasks.size) {
                val task = allTasks.get(i);
                if(task.title.toString().trim().matches(text.toString().trim().toRegex())){
                    tasks!!.add(task)
                    break
                }
                // body of loop
            }
            if(tasks!!.size>0)
                populateTasks(tasks)
        }

    }

    override fun onButtonClicked(buttonCode: Int) {
buttonCode
    }
}
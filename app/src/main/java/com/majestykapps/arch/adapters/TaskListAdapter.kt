package com.majestykapps.arch.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.majestykapps.arch.R
import com.majestykapps.arch.databinding.RowTasksBinding
import com.majestykapps.arch.databinding.RowTasksBindingImpl
import com.majestykapps.arch.domain.entity.Task
import com.majestykapps.arch.presentation.tasks.onTaskClickListner

class TaskListAdapter(val mContext: Context, val mTaskList: List<Task>,val onTaskClickListner: onTaskClickListner) :
    RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskViewHolder (
       DataBindingUtil.inflate<RowTasksBinding>(LayoutInflater.from(mContext),R.layout.row_tasks, parent, false)
    )


    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val rowDataJObj = mTaskList.get(position);
        holder.rowTasksBinding.tvTitle.text = rowDataJObj.title
        holder.rowTasksBinding.tvDescription.text = rowDataJObj.description
        holder.rowTasksBinding.root.setOnClickListener {
            onTaskClickListner.onTaskItemClickListner(rowDataJObj.id.toString())
        }
    }

    override fun getItemCount(): Int {
        return mTaskList.size
    }

}



class TaskViewHolder(val rowTasksBinding: RowTasksBinding) : RecyclerView.ViewHolder(rowTasksBinding.root)
package com.datnt.app_quanlycongviec

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class TaskAdapter(private var taskList: List<Task>): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    class TaskViewHolder(view: View): RecyclerView.ViewHolder(view){
        private var tvTitle: TextView = view.findViewById(R.id.taskTitle)
        private var tvDetails: TextView = view.findViewById(R.id.taskDetails)
        private var tvDate: TextView = view.findViewById(R.id.tvDate)

        fun setData(task: Task){
            tvTitle.text = task.name
            tvDetails.text = task.details
            tvDate.text = task.time
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.setData(taskList[position])
    }
}
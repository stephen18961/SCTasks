package com.example.SCTasks

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private var tasks: ArrayList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private lateinit var clickListener:onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        clickListener = listener
    }

    class TaskViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.taskName)
        val descTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        val viewSquare: View = itemView.findViewById(R.id.viewSquare)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)
        return TaskViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.title
        holder.descTextView.text = task.description

        // Change the color of viewSquare based on the task status
        val statusColor = when (task.status) {
            "New" -> android.R.color.holo_red_light // Red color for status new
            "In Progress" -> android.R.color.holo_orange_light // Yellow color for status in progress
            "Done" -> android.R.color.holo_green_light // Green color for status done
            else -> android.R.color.transparent // Default color if status is unknown
        }
        holder.viewSquare.setBackgroundResource(statusColor)
    }

    override fun getItemCount() = tasks.size

    // Method to update the tasks list and notify the adapter
    @SuppressLint("NotifyDataSetChanged")
    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}

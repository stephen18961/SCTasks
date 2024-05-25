package com.example.SCTasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TaskFragment.kt
class TaskFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private var taskStatus: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            taskStatus = it.getString("status")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        taskAdapter = TaskAdapter(getTasksByStatus(taskStatus))
        Log.e("TAG", "onViewCreated: ${getTasksByStatus(taskStatus)}")
        recyclerView.adapter = taskAdapter
    }

    private fun getTasksByStatus(status: String?): List<Task> {
        // Replace this with your actual data source
        val allTasks = listOf(
            Task(1, "My New Task", "This is a new task", "Important", "new", "2023-08-01", "2023-08-15", "2 hour"),
            Task(2, "My In Progress", "This is a new task", "Important", "In Progress", "2023-08-01", "2023-08-15", "2 hour"),
            Task(3, "My Done", "This is a new task", "Important", "done", "2023-08-01", "2023-08-15", "2 hour")
        )

        Log.d("Status", "status: $status")
        val selectedTasks = mutableListOf<Task>()
        for (task in allTasks) {
            if (task.status.equals(status, ignoreCase = true)) {
                selectedTasks.add(task)
            }
        }
        return selectedTasks
    }

    companion object {
        private const val ARG_STATUS = "status"

        @JvmStatic
        fun newInstance(status: String) =
            TaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_STATUS, status)
                }
            }
    }
}

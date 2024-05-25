package com.example.SCTasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TaskFragment.kt
class TaskFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels()
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

        taskAdapter = TaskAdapter(mutableListOf())
        recyclerView.adapter = taskAdapter

        taskViewModel.tasks.observe(viewLifecycleOwner, { taskList ->
            val filteredTasks = getTasksByStatus(taskStatus)
            taskAdapter.updateTasks(filteredTasks)
        })

        taskViewModel.getTasks()
    }

    private fun getTasksByStatus(status: String?): List<Task> {
        val tasks = taskViewModel.tasks.value ?: emptyList()  // Get the current value of LiveData or an empty list if null

        val selectedTasks = mutableListOf<Task>()

        for (task in tasks) {
            if (task.status.equals(status, ignoreCase = true)) {
                selectedTasks.add(task)
            }
        }
        Log.d("TaskFragment", "All tasks: $tasks")
        Log.d("TaskFragment", "Selected tasks: $selectedTasks")

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

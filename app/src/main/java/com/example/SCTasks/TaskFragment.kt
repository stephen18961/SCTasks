package com.example.SCTasks

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class TaskFragment : Fragment() {

    private val allTasksInfoModels = ArrayList<Task>()
    private val tasksInfoModels = ArrayList<Task>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter

    private val taskViewModel: TaskViewModel by viewModels()

    private var filterStatus: String? = null // Variable to hold the status filter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = TaskAdapter(tasksInfoModels)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.setOnItemClickListener(object : TaskAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val bundle = Bundle()
                val taskData = tasksInfoModels[position]

                taskData.id?.let { bundle.putInt("id", it) }
                bundle.putString("title", taskData.title)
                bundle.putString("description", taskData.description)
                bundle.putString("category", taskData.category)
                bundle.putString("status", taskData.status)
                bundle.putString("createdTime", taskData.createdTime)
                bundle.putString("finishedTime", taskData.finishedTime)
                bundle.putString("duration", taskData.duration)

                findNavController().navigate(R.id.action_taskFragment_to_taskDetailsFragment, bundle)
            }
        })

        // Get the status filter from arguments
        filterStatus = arguments?.getString("status")

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.normalButton -> {
                    filterTasksByStatusAndCategory(filterStatus,"Normal")
                    true
                }
                R.id.urgentButton -> {
                    filterTasksByStatusAndCategory(filterStatus,"Urgent")
                    true
                }
                R.id.importantButton -> {
                    filterTasksByStatusAndCategory(filterStatus,"Important")
                    true
                }
                else -> false
            }
        }

        // Observe the tasks LiveData
        taskViewModel.tasks.observe(viewLifecycleOwner) { taskList ->
            // Update the RecyclerView with the new task list
            allTasksInfoModels.clear()
            allTasksInfoModels.addAll(taskList)
            filterTasksByStatusAndCategory(filterStatus, "Normal")
        }

        // Load tasks from ViewModel
        taskViewModel.getTasks()

        return view
    }


    private fun filterTasksByStatusAndCategory(status: String?, category: String) {
        val filteredTasks = allTasksInfoModels.filter { it.status == status && it.category == category }
        updateRecyclerView(filteredTasks)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateRecyclerView(filteredTasks: List<Task>) {
        tasksInfoModels.clear()
        tasksInfoModels.addAll(filteredTasks)
        adapter.notifyDataSetChanged()
    }
}
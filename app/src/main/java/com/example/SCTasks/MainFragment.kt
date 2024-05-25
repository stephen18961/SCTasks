package com.example.SCTasks

import TaskViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

class MainFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskViewModel.getTasks()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val btnNew: LinearLayout = view.findViewById(R.id.newButton)
        val btnInProgress: LinearLayout = view.findViewById(R.id.inProgressButton)
        val btnDone: LinearLayout = view.findViewById(R.id.doneButton)

        btnNew.setOnClickListener { navigateToTaskFragment("New") }
        btnInProgress.setOnClickListener { navigateToTaskFragment("In Progress") }
        btnDone.setOnClickListener { navigateToTaskFragment("Done") }

        val addTask =
            view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(
                R.id.addTaskButton
            )


        addTask.setOnClickListener {
            // ask for a nav controller
            val navController = view.findNavController()
            // navigate into certain destination
            navController.navigate(R.id.action_mainFragment_to_newTaskFragment)
        }

        // Observe the tasks data
        taskViewModel.tasks.observe(viewLifecycleOwner, Observer { taskList ->
            // Update UI with taskList
            Log.e("MainFragment", "Observed task list: $taskList")
        })

        // Observe status counts
        taskViewModel.statusZeroCount.observe(viewLifecycleOwner, Observer { count ->
            Log.e("MainFragment", "Status 0 count: $count")
            view.findViewById<TextView>(R.id.textNewCount).text = "$count tasks"
        })

        taskViewModel.statusOneCount.observe(viewLifecycleOwner, Observer { count ->
            Log.e("MainFragment", "Status 1 count: $count")
            view.findViewById<TextView>(R.id.textInProgressCount).text = "$count tasks"
        })

        taskViewModel.statusTwoCount.observe(viewLifecycleOwner, Observer { count ->
            Log.e("MainFragment", "Status 2 count: $count")
            view.findViewById<TextView>(R.id.textDoneCount).text = "$count tasks"
        })

        return view
    }

    private fun navigateToTaskFragment(status: String) {
        val bundle = Bundle()
        bundle.putString("status", status)
        Log.d("TEST", "bundle: $bundle")
        findNavController().navigate(R.id.action_mainFragment_to_taskFragment, bundle)
    }
}
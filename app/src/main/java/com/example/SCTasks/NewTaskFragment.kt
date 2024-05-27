package com.example.SCTasks

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.SCTasks.ApiClient.taskApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [NewTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewTaskFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_task, container, false)

        val taskCategory = view.findViewById<AutoCompleteTextView>(R.id.taskCategory)
        val taskTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val taskDescription = view.findViewById<EditText>(R.id.editTextDescription)
        val addTaskButton = view.findViewById<Button>(R.id.addTaskButton)
        val backToMainButton = view.findViewById<Button>(R.id.cancelButton)
        val category = resources.getStringArray(R.array.categoryTypes)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, category)
        taskCategory.setAdapter(arrayAdapter)

        addTaskButton.setOnClickListener {
            // ask for a nav controller
            val navController = view.findNavController()
            // navigate into certain destination
            navController.navigate(R.id.action_newTaskFragment_to_mainFragment)
            // Check if any field is empty
            if (taskTitle.text.isEmpty() || taskDescription.text.isEmpty() || taskCategory.text.isEmpty()) {
                // Show a Toast message if any field is empty
                Toast.makeText(requireContext(), "Please fill all fields to add a task", Toast.LENGTH_SHORT).show()
            } else {
                val category = taskCategory.text.toString()

                val createdTime = System.currentTimeMillis()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val dateString = dateFormat.format(createdTime)

                val task = Task(
                    title = taskTitle.text.toString(),
                    description = taskDescription.text.toString(),
                    status = "New",
                    category = category,
                    createdTime = dateString
                )
                addTask(task)
                view.findNavController()
            }
        }

        backToMainButton.setOnClickListener {
            // Ask for a nav controller
            val navController = view.findNavController()
            // Navigate to the main fragment
            navController.navigate(R.id.action_newTaskFragment_to_mainFragment)
        }
        return view
    }

    private fun addTask(task: Task) {
        val call = taskApi.postTask(task)

        call.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>, t: Throwable) {
                Log.e("NewTaskFragment", "Failed to add task", t)
            }

            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful) {
                    val addedTask = response.body()
                    Log.e("NewTaskFragment", "Task added successfully \n $addedTask")
                    taskViewModel.getTasks()
                } else {
                    Log.e("NewTaskFragment", "Failed to add task \n ${response.errorBody()?.string() ?: ""}")
                }
            }
        })
    }
}

package com.example.SCTasks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SCTasks.ApiClient.taskApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskViewModel : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _statusNewCount = MutableLiveData<Int>()
    val statusNewCount: LiveData<Int> get() = _statusNewCount

    private val _statusInProgressCount = MutableLiveData<Int>()
    val statusInProgressCount: LiveData<Int> get() = _statusInProgressCount

    private val _statusDoneCount = MutableLiveData<Int>()
    val statusDoneCount: LiveData<Int> get() = _statusDoneCount

    init {
        getTasks()
    }
    fun setTasks(taskList: List<Task>) {
        _tasks.value = taskList
        updateStatusCounts(taskList)
    }

    fun getTasks() {
        val call = taskApi.getTasks()

        call.enqueue(object : Callback<TaskResponse> {
            override fun onFailure(call: Call<TaskResponse>, t: Throwable) {
                Log.e("com.example.SCTasks.TaskViewModel", "Failed to get tasks", t)
            }

            override fun onResponse(call: Call<TaskResponse>, response: Response<TaskResponse>) {
                if (response.isSuccessful) {
                    val taskList = response.body()?.tasks ?: emptyList()
                    setTasks(taskList)
                    Log.d("Tasks retrieved successfully", _tasks.value.toString())
                } else {
                    Log.e("com.example.SCTasks.TaskViewModel", "Failed to get tasks \n ${response.errorBody()?.string() ?: ""}")
                }
            }
        })
    }

    private fun updateStatusCounts(taskList: List<Task>) {
        val statusNew = taskList.count { it.status == "New" }
        val statusOne = taskList.count { it.status == "In Progress" }
        val statusTwo = taskList.count { it.status == "Done" }

        _statusNewCount.value = statusNew
        _statusInProgressCount.value = statusOne
        _statusDoneCount.value = statusTwo

        Log.d("com.example.SCTasks.TaskViewModel", "Status counts updated - NEW: $statusNew, INPROG: $statusOne, DONE: $statusTwo")
    }

    fun updateTaskStatus(taskId: Int, newStatus: String) {
        val statusUpdate = TaskStatusUpdate(newStatus)
        val call = taskApi.updateTaskStatus(taskId, statusUpdate)

        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("com.example.SCTasks.TaskViewModel", "Failed to update task status", t)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Refresh tasks after successful update
                    getTasks()
                    Log.d("com.example.SCTasks.TaskViewModel", "Task status updated successfully")
                } else {
                    Log.e("com.example.SCTasks.TaskViewModel", "Failed to update task status \n ${response.errorBody()?.string() ?: ""}")
                }
            }
        })
    }
}

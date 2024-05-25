import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SCTasks.ApiClient.taskApiService
import com.example.SCTasks.Task
import com.example.SCTasks.TaskResponse
import com.example.SCTasks.TaskStatusUpdate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskViewModel : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _statusZeroCount = MutableLiveData<Int>()
    val statusZeroCount: LiveData<Int> get() = _statusZeroCount

    private val _statusOneCount = MutableLiveData<Int>()
    val statusOneCount: LiveData<Int> get() = _statusOneCount

    private val _statusTwoCount = MutableLiveData<Int>()
    val statusTwoCount: LiveData<Int> get() = _statusTwoCount
    fun setTasks(taskList: List<Task>) {
        _tasks.value = taskList
        updateStatusCounts(taskList)
    }

    fun getTasks() {
        val call = taskApiService.getTasks()

        call.enqueue(object : Callback<TaskResponse> {
            override fun onFailure(call: Call<TaskResponse>, t: Throwable) {
                Log.e("TaskViewModel", "Failed to get tasks", t)
            }

            override fun onResponse(call: Call<TaskResponse>, response: Response<TaskResponse>) {
                if (response.isSuccessful) {
                    val taskList = response.body()?.task ?: emptyList()
                    setTasks(taskList)
                } else {
                    Log.e("TaskViewModel", "Failed to get tasks \n ${response.errorBody()?.string() ?: ""}")
                }
            }
        })
    }

    private fun updateStatusCounts(taskList: List<Task>) {
        val statusZero = taskList.count { it.status == "0" }
        val statusOne = taskList.count { it.status == "1" }
        val statusTwo = taskList.count { it.status == "2" }

        _statusZeroCount.value = statusZero
        _statusOneCount.value = statusOne
        _statusTwoCount.value = statusTwo

        Log.d("TaskViewModel", "Status counts updated - 0: $statusZero, 1: $statusOne, 2: $statusTwo")
    }

    fun updateTaskStatus(taskId: Int, newStatus: String) {
        val statusUpdate = TaskStatusUpdate(newStatus)
        val call = taskApiService.updateTaskStatus(taskId, statusUpdate)

        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("TaskViewModel", "Failed to update task status", t)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Refresh tasks after successful update
                    getTasks()
                    Log.d("TaskViewModel", "Task status updated successfully")
                } else {
                    Log.e("TaskViewModel", "Failed to update task status \n ${response.errorBody()?.string() ?: ""}")
                }
            }
        })
    }
}

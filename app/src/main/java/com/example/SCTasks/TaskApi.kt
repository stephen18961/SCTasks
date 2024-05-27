package com.example.SCTasks

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {

    @GET("/tasks")
    fun getTasks(): Call<TaskResponse>

    @POST("/new_task")
    fun postTask(@Body task: Task): Call<Task>

    @PUT("/tasks/{id}")
    fun updateTaskStatus(@Path("id") id: Int, @Body statusUpdate: TaskStatusUpdate): Call<Void>

    @DELETE("/tasks/{id}")
    fun deleteTask(@Path("id") id: Int): Call<Void>
}

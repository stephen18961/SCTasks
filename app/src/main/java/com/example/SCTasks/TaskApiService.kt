package com.example.SCTasks

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApiService {

    @GET("/tasks")
    fun getTasks(): Call<TaskResponse>

    @POST("/tasks")
    fun postTask(@Body task: Task): Call<Task>

    @PUT("/tasks/{id}")
    fun updateTaskStatus(@Path("id") id: Int, @Body statusUpdate: TaskStatusUpdate): Call<Void>
}
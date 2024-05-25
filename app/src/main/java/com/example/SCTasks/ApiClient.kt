package com.example.SCTasks

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5000")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val taskApiService: TaskApiService = retrofit.create(TaskApiService::class.java)
}
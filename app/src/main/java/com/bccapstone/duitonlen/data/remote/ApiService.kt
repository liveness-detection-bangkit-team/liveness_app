package com.bccapstone.duitonlen.data.remote

import com.bccapstone.duitonlen.data.models.ApiResponse
import com.bccapstone.duitonlen.data.models.LoginRequest
import com.bccapstone.duitonlen.data.models.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse
}
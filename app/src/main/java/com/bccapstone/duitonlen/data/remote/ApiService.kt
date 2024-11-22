package com.bccapstone.duitonlen.data.remote

import com.bccapstone.duitonlen.data.models.LoginRequest
import com.bccapstone.duitonlen.data.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
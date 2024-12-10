package com.bccapstone.duitonlen.data.repository

import com.bccapstone.duitonlen.data.models.ApiResponse
import com.bccapstone.duitonlen.data.models.RegisterRequest
import com.bccapstone.duitonlen.data.remote.ApiService
import javax.inject.Inject

class RegisterRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun register(username: String, password: String, fullname: String): ApiResponse {
        return apiService.register(RegisterRequest(username, password, fullname))
    }
}
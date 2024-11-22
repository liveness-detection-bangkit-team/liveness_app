package com.bccapstone.duitonlen.data.repository

import com.bccapstone.duitonlen.data.models.ApiResponse
import com.bccapstone.duitonlen.data.models.LoginRequest
import com.bccapstone.duitonlen.data.remote.ApiService
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(username: String, password: String): ApiResponse {
        return apiService.login(LoginRequest(username, password))
    }
}

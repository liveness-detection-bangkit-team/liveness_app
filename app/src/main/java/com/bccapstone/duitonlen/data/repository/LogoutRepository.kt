package com.bccapstone.duitonlen.data.repository

import com.bccapstone.duitonlen.data.models.ApiResponse
import com.bccapstone.duitonlen.data.models.LogoutRequest
import com.bccapstone.duitonlen.data.remote.ApiService
import javax.inject.Inject

class LogoutRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun logout(): ApiResponse {
        return apiService.logout()
    }
}
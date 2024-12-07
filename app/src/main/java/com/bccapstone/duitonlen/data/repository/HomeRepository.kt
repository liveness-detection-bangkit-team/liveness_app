package com.bccapstone.duitonlen.data.repository

import com.bccapstone.duitonlen.data.models.ApiResponse
import com.bccapstone.duitonlen.data.remote.ApiService
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getHome(): ApiResponse {
        return apiService.getHome()
    }
}

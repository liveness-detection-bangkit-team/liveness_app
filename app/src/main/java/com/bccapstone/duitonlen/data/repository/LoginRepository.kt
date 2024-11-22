package com.bccapstone.duitonlen.data.repository

import retrofit2.HttpException
import com.bccapstone.duitonlen.data.models.LoginRequest
import com.bccapstone.duitonlen.data.models.LoginResponse
import com.bccapstone.duitonlen.data.remote.ApiService
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(username: String, password: String): LoginResponse {
        return apiService.login(LoginRequest(username, password))
    }
}

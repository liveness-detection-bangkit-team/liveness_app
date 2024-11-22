package com.bccapstone.duitonlen.data.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("password")
    val password: String
)

data class LoginResponse(
        @field:SerializedName("message")
        val message: String,

        @field:SerializedName("status_code")
        val statusCode: Int,
)

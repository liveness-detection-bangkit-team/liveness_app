package com.bccapstone.duitonlen.data.models

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status_code")
    val statusCode: Int,
)

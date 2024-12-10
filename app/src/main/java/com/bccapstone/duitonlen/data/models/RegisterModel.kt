package com.bccapstone.duitonlen.data.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(

    @field:SerializedName("fullname")
    val fullname: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("password")
    val password: String,
)
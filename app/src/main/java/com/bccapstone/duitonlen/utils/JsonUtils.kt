package com.bccapstone.duitonlen.utils

import org.json.JSONObject

fun String.getErrorMessage(): String {
    return try {
        val jsonObject = JSONObject(this)
        jsonObject.getString("message")
    } catch (e: Exception) {
        "An error occurred"
    }
}
package com.bccapstone.duitonlen.presentation.screens.auth.register

import retrofit2.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bccapstone.duitonlen.data.models.ApiResponse
import com.bccapstone.duitonlen.data.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.bccapstone.duitonlen.data.Result
import com.bccapstone.duitonlen.utils.getErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
): ViewModel() {
    private val _registerResult = MutableStateFlow<Result<ApiResponse>>(Result.Idle)
    val registerState = _registerResult.asStateFlow()

    fun register(username: String, password: String, fullname: String) {
        viewModelScope.launch {
            _registerResult.value = Result.Loading
            try {
                val response = registerRepository.register(username, password, fullname).also {
                    // check if the response is not successful
                    if (it.statusCode != 201) {
                        throw HttpException(Response.error<ApiResponse>(it.statusCode, it.message.toResponseBody()))
                    }
                }
                _registerResult.value = Result.Success(response)
            } catch (e: HttpException) {
                // handle the error from api, shows the message from the json response
                val message = e.response()?.errorBody()?.string()?.getErrorMessage() ?: "An error occurred"
                _registerResult.value = Result.Error(message)
            } catch (e: Exception) {
                // handle other errors
                _registerResult.value = Result.Error(e.message ?: "An error occurred")
            }
        }
    }
}
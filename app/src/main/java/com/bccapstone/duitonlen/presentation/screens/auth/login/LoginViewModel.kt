package com.bccapstone.duitonlen.presentation.screens.auth.login

import retrofit2.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bccapstone.duitonlen.data.Result
import com.bccapstone.duitonlen.data.models.ApiResponse
import com.bccapstone.duitonlen.data.repository.LoginRepository
import com.bccapstone.duitonlen.utils.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loginResult = MutableStateFlow<Result<ApiResponse>>(Result.Idle)
    val loginState = _loginResult.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Result.Loading
            try {
                val response = loginRepository.login(username, password).also {
                    // check if the response is not successful
                    if (it.statusCode != 200) {
                        throw HttpException(Response.error<ApiResponse>(it.statusCode, it.message.toResponseBody()))
                    }
                }
                _loginResult.value = Result.Success(response)
            } catch (e: HttpException) {
                // handle the error from api, shows the message from the json response
                val message = e.response()?.errorBody()?.string()?.getErrorMessage() ?: "An error occurred"
                _loginResult.value = Result.Error(message)
            } catch (e: Exception) {
                // handle other errors
                _loginResult.value = Result.Error(e.message ?: "An error occurred")
            }
        }
    }
}

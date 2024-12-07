package com.bccapstone.duitonlen.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bccapstone.duitonlen.data.models.ApiResponse
import com.bccapstone.duitonlen.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import com.bccapstone.duitonlen.data.Result
import com.bccapstone.duitonlen.data.local.DuitOnlenCookieJar
import com.bccapstone.duitonlen.data.repository.LogoutRepository
import com.bccapstone.duitonlen.utils.getErrorMessage
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val logoutRepository: LogoutRepository
) : ViewModel() {
    private val cookieJar = DuitOnlenCookieJar()

    private val _home = MutableStateFlow<Result<ApiResponse>>(Result.Idle)
    val homeState = _home.asStateFlow()

    private val _logout = MutableStateFlow<Result<ApiResponse>>(Result.Idle)
    val logoutState = _logout.asStateFlow()

    fun getHome() {
        viewModelScope.launch {
            _home.value = Result.Loading
            try {
                val response = homeRepository.getHome().also {
                    // check if the response is not successful
                    if (it.statusCode != 200) {
                        throw HttpException(Response.error<ApiResponse>(it.statusCode, it.message.toResponseBody()))
                    }
                }
                _home.value = Result.Success(response)
            } catch (e: HttpException) {
                // handle the error from api, shows the message from the json response
                    val message = e.response()?.errorBody()?.string()?.getErrorMessage() ?: "An error occurred"
                    _home.value = Result.Error(message)
            } catch (e: Exception) {
                // handle other errors
                _home.value = Result.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logout.value = Result.Loading
            try {
                val response = logoutRepository.logout().also {
                    // check if the response is not successful
                    if (it.statusCode != 200) {
                        throw HttpException(Response.error<ApiResponse>(it.statusCode, it.message.toResponseBody()))
                    }
                }
                _logout.value = Result.Success(response)
            } catch (e: HttpException) {
                // handle the error from api, shows the message from the json response
                    val message = e.response()?.errorBody()?.string()?.getErrorMessage() ?: "An error occurred"
                    _logout.value = Result.Error(message)
            } catch (e: Exception) {
                // handle other errors
                _logout.value = Result.Error(e.message ?: "An error occurred")
            }
        }

        // ensure that the cookies are cleared
        cookieJar.clearCookies()
    }
}
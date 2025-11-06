package com.lephiha.fitx_06

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lephiha.fitx_06.Container.AuthResponse
import com.lephiha.fitx_06.Repository.AuthRepository

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _registerState = MutableLiveData<AuthState>()
    val registerState: LiveData<AuthState> = _registerState

    private val _loginState = MutableLiveData<AuthState>()
    val loginState: LiveData<AuthState> = _loginState

    private val _forgotPasswordState = MutableLiveData<AuthState>()
    val forgotPasswordState: LiveData<AuthState> = _forgotPasswordState

    private val _resetPasswordState = MutableLiveData<AuthState>()
    val resetPasswordState: LiveData<AuthState> = _resetPasswordState

    fun register(email: String, password: String, name: String? = null, phone: String? = null) {
        _registerState.value = AuthState.Loading

        repository.register(
            email = email,
            password = password,
            name = name,
            phone = phone,
            onSuccess = { response ->
                _registerState.value = AuthState.Success(response)
            },
            onError = { error ->
                _registerState.value = AuthState.Error(error)
            }
        )
    }

    fun login(email: String, password: String) {
        _loginState.value = AuthState.Loading

        repository.login(
            email = email,
            password = password,
            onSuccess = { response ->
                _loginState.value = AuthState.Success(response)
            },
            onError = { error ->
                _loginState.value = AuthState.Error(error)
            }
        )
    }

    fun forgotPassword(email: String) {
        _forgotPasswordState.value = AuthState.Loading

        repository.forgotPassword(
            email = email,
            onSuccess = { response ->
                _forgotPasswordState.value = AuthState.Success(response)
            },
            onError = { error ->
                _forgotPasswordState.value = AuthState.Error(error)
            }
        )
    }

    fun resetPassword(token: String, newPassword: String) {
        _resetPasswordState.value = AuthState.Loading

        repository.resetPassword(
            token = token,
            newPassword = newPassword,
            onSuccess = { response ->
                _resetPasswordState.value = AuthState.Success(response)
            },
            onError = { error ->
                _resetPasswordState.value = AuthState.Error(error)
            }
        )
    }

    fun resetRegisterState() {
        _registerState.value = AuthState.Idle
    }

    fun resetLoginState() {
        _loginState.value = AuthState.Idle
    }

    fun resetForgotPasswordState() {
        _forgotPasswordState.value = AuthState.Idle
    }

    fun resetResetPasswordState() {
        _resetPasswordState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val response: AuthResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}
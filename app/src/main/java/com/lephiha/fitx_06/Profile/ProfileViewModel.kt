package com.lephiha.fitx_06

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lephiha.fitx_06.Container.UserProfileResponse
import com.lephiha.fitx_06.Repository.UserRepository

class ProfileViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _profileState = MutableLiveData<ProfileState>()
    val profileState: LiveData<ProfileState> = _profileState

    fun loadUserProfile(token: String) {
        _profileState.value = ProfileState.Loading

        repository.getUserProfile(
            token = token,
            onSuccess = { response ->
                _profileState.value = ProfileState.Success(response)
            },
            onError = { error ->
                _profileState.value = ProfileState.Error(error)
            }
        )
    }

    fun updateProfile(token: String, profileData: Map<String, Any>) {
        _profileState.value = ProfileState.Loading

        repository.updateUserProfile(
            token = token,
            profileData = profileData,
            onSuccess = { response ->
                _profileState.value = ProfileState.Success(response)
            },
            onError = { error ->
                _profileState.value = ProfileState.Error(error)
            }
        )
    }

    fun resetState() {
        _profileState.value = ProfileState.Idle
    }
}

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val response: UserProfileResponse) : ProfileState()
    data class Error(val message: String) : ProfileState()
}
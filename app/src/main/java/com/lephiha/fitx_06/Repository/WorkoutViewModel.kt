package com.lephiha.fitx_06

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lephiha.fitx_06.Container.WorkoutScheduleResponse
import com.lephiha.fitx_06.Repository.WorkoutRepository

class WorkoutViewModel : ViewModel() {

    private val repository = WorkoutRepository()

    private val _workoutState = MutableLiveData<WorkoutState>()
    val workoutState: LiveData<WorkoutState> = _workoutState

    fun loadWorkoutSchedules(token: String) {
        _workoutState.value = WorkoutState.Loading

        repository.getWorkoutSchedules(
            token = token,
            onSuccess = { response ->
                _workoutState.value = WorkoutState.Success(response)
            },
            onError = { error ->
                _workoutState.value = WorkoutState.Error(error)
            }
        )
    }

    fun checkInWorkout(token: String, workoutId: Int) {
        _workoutState.value = WorkoutState.Loading

        repository.checkInWorkout(
            token = token,
            workoutId = workoutId,
            onSuccess = { response ->
                _workoutState.value = WorkoutState.CheckInSuccess(response)
            },
            onError = { error ->
                _workoutState.value = WorkoutState.Error(error)
            }
        )
    }

    fun resetState() {
        _workoutState.value = WorkoutState.Idle
    }
}

sealed class WorkoutState {
    object Idle : WorkoutState()
    object Loading : WorkoutState()
    data class Success(val response: WorkoutScheduleResponse) : WorkoutState()
    data class CheckInSuccess(val response: WorkoutScheduleResponse) : WorkoutState()
    data class Error(val message: String) : WorkoutState()
}
package com.bumperpick.bumperickUser.Screens.Splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumperpick.bumperickUser.Navigation.Screen
import com.bumperpick.bumperickUser.Repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import  com.bumperpick.bumperickUser.Repository.Result
sealed class SplashState {
    object Loading : SplashState()
    data class Success(val screen: Screen) : SplashState()
    data class Error(val message: String) : SplashState()
}

class SplashViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginCheckState = MutableStateFlow<SplashState>(SplashState.Loading)
    val loginCheckState: StateFlow<SplashState> get() = _loginCheckState

    init {
        updateToken()
        checkIfAlreadyLoggedIn()
    }

    private fun updateToken() {
        viewModelScope.launch {
            try {
                authRepository.updatefirbase_token()
            } catch (e: Exception) {
                // Optional: handle token update failure silently
            }
        }
    }

    private fun checkIfAlreadyLoggedIn() {
        viewModelScope.launch {
            // Add a short splash delay (e.g., 2 seconds)
            delay(2000)
            when (val result = authRepository.checkAlreadyLogin()) {
                is Result.Success -> {
                    val targetScreen = if (result.data) Screen.HomePage else Screen.StartScreen
                    _loginCheckState.value = SplashState.Success(targetScreen)
                }
                is Result.Error -> {
                    _loginCheckState.value = SplashState.Error(result.message)
                }
                Result.Loading -> {
                    _loginCheckState.value = SplashState.Loading
                }
            }
        }
    }
}

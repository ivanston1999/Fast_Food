package com.android.fastfood.presentation.auth.splashscreen

import androidx.lifecycle.ViewModel
import com.android.fastfood.presentation.auth.firebase.repository.UserRepository

class SplashScreenViewModel(private val repository: UserRepository) : ViewModel() {

    fun isUserLoggedIn() = repository.isLoggedIn()

}
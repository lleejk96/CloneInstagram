package com.gitauto.cloneinstgram.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitauto.cloneinstgram.repository.LoginRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    var resultLogin = loginRepository.resultLogin
    var resultSingIn = loginRepository.resultSingIn
    var resultUser = loginRepository.resultUser

    fun requestSignup(email : String, password : String){
        viewModelScope.launch {
            loginRepository.requestSignup(email, password)
        }
    }
    fun requestSingIn(email : String, password : String){
        viewModelScope.launch {
            loginRepository.requestSingIn(email, password)
        }
    }
    fun requestGoogleSignIn(credential: AuthCredential){
        viewModelScope.launch {
            loginRepository.requestSingIn(credential)
        }
    }
    fun requestCurrentUser(){
        viewModelScope.launch {
            loginRepository.requestCurrentUser()
        }
    }
}
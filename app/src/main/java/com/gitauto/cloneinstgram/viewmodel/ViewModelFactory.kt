package com.gitauto.cloneinstgram.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gitauto.cloneinstgram.repository.ContentRepository
import com.gitauto.cloneinstgram.repository.LoginRepository

class MainViewModelFactory : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            MainViewModel(ContentRepository()) as T
        } else if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            LoginViewModel(LoginRepository()) as T
        }
        else{
            throw IllegalArgumentException("Unkwon ViewModel class")
        }
    }
}
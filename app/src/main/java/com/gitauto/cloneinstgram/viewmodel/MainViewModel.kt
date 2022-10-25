package com.gitauto.cloneinstgram.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitauto.cloneinstgram.repository.ContentRepository
import kotlinx.coroutines.launch
import java.text.FieldPosition

class MainViewModel(private val contentRepository: ContentRepository) : ViewModel() {
    var contentLiveData = contentRepository.contentDTOLiveData
    var contentUidListLiveData = contentRepository.contentUidListLiveData
    var contentFavoriteLiveData = contentRepository.contentFavoriteLiveData
    var userContentLiveData = contentRepository.userContentDTOLiveData

    fun requestContentData(){
        viewModelScope.launch {
            contentRepository.requestContent()
        }
    }
    fun requestUserContentData(uid : String?){
        viewModelScope.launch {
            contentRepository.requestUserContent(uid)
        }
    }
    fun uploadFavorite(position: Int){
        Log.d("TAG", "uploadFavorite viewmodel: ")
        contentRepository.uploadFavorite(position)
    }
    fun requestLogOut(){
        viewModelScope.launch {
            contentRepository.requestLogOut()
        }
    }
}
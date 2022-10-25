package com.gitauto.cloneinstgram.data

data class Result<T>(val status: Status, val data: T?, val errorCode: String?)

enum class Status{
    SUCCESS,
    FAIL,
    CANCEL
}
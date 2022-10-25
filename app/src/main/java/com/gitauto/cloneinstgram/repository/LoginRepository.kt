package com.gitauto.cloneinstgram.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gitauto.cloneinstgram.data.Result
import com.gitauto.cloneinstgram.data.Status
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginRepository {
    private var auth : FirebaseAuth? = null

    var resultLogin = MutableLiveData<Result<String>>()
    var resultSingIn = MutableLiveData<Result<String>>()
    var resultUser = MutableLiveData<FirebaseUser>()
    init {
        auth = FirebaseAuth.getInstance()

    }
    fun requestSignup(email : String, password : String){
        CoroutineScope(Dispatchers.IO).launch {
            auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener {
                    if (it.isSuccessful){
                        resultLogin.postValue(Result(Status.SUCCESS, "success", null))
                    }else{
                        resultLogin.postValue(Result(Status.FAIL, null, it.exception?.message))
                    }
                }
        }
    }
    fun requestSingIn(email : String, password : String){
        CoroutineScope(Dispatchers.IO).launch {
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d("TAG", "requestSingIn: 1")
                        resultSingIn.postValue(Result(Status.SUCCESS, "success", null))
                    }else{
                        Log.d("TAG", "requestSingIn: 2 ${it.exception?.message}")
                        resultSingIn.postValue(Result(Status.FAIL, null, it.exception?.message))
                    }
                }
        }
    }
    fun requestSingIn(credential: AuthCredential){
        CoroutineScope(Dispatchers.IO).launch {
            auth?.signInWithCredential(credential)
                ?.addOnCompleteListener {
                    if (it.isSuccessful){
                        resultSingIn.postValue(Result(Status.SUCCESS, "success", null))
                    }else{
                        resultSingIn.postValue(Result(Status.FAIL, null, it.exception?.message))
                    }
                }
        }
    }
    fun requestCurrentUser(){
        CoroutineScope(Dispatchers.IO).launch {
            resultUser.postValue(auth?.currentUser)
        }
    }
}
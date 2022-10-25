package com.gitauto.cloneinstgram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.gitauto.cloneinstgram.data.Status
import com.gitauto.cloneinstgram.viewmodel.LoginViewModel
import com.gitauto.cloneinstgram.viewmodel.MainViewModel
import com.gitauto.cloneinstgram.viewmodel.MainViewModelFactory
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.util.jar.Manifest

class LoginActivity : AppCompatActivity() {
    companion object{
        const val GOOGLE_LOGIN_CODE = 9001
        const val TAG = "TAG"
    }
    private var googleSignInClient : GoogleSignInClient? = null
    private val viewModel : LoginViewModel by lazy{
        ViewModelProvider(this, MainViewModelFactory()).get(LoginViewModel::class.java)
    }
    private lateinit var getResult : ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initData()
        initView()
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("733223252250-p6g9hrp1qsg6feq53o504jifugomgtu1.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            Log.d(TAG, "onCreate: ${it.resultCode}")
//            if (it.resultCode == GOOGLE_LOGIN_CODE){z
                val result = it.data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
                if (result?.isSuccess == true){
                    val account  = result.signInAccount
                    firebaseAuthWithGoogle(account)
                }
            }
//        }
    }

    override fun onStart() {
        super.onStart()
        requestCurrentUser()
    }

    private fun firebaseAuthWithGoogle(account : GoogleSignInAccount?){
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        viewModel.requestGoogleSignIn(credential)
    }
    private fun googleLogin(){
        val singInIntent = googleSignInClient?.signInIntent
        getResult.launch(singInIntent)
    }
    private fun requestCurrentUser(){
        viewModel.requestCurrentUser()
    }

    private fun initData(){
        viewModel.resultLogin.observe(this, {
            if (it.status == Status.SUCCESS){
                movePage()
            } else{
                viewModel.requestSingIn(emailText.text.toString().trim(), passwordText.text.toString())
            }
        })
        viewModel.resultSingIn.observe(this, {
            if (it.status == Status.SUCCESS){
                Log.d("TAG", "initData: success")
                movePage()
            }else{
                Toast.makeText(this, "Already register email", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.resultUser.observe(this, {
//            moveMainPage(it)
        })
    }
    private fun initView(){
        normalLogin.setOnClickListener {
            if (!emailText.text.isNullOrEmpty() && !passwordText.text.isNullOrEmpty())
                viewModel.requestSignup(emailText.text.toString().trim(), passwordText.text.toString())
            else
                Toast.makeText(this, "Empty Email or Password", Toast.LENGTH_SHORT).show()
        }
        googleSign.setOnClickListener {
            googleLogin()
        }
    }
    private fun movePage(){
        startActivity(Intent(this, MainActivity::class.java))
    }
    private fun moveMainPage(user : FirebaseUser){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
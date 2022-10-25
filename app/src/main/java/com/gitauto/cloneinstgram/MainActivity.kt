package com.gitauto.cloneinstgram

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.gitauto.cloneinstgram.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(){
    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val detailViewFragment = DetailViewFragment()
        supportFragmentManager.beginTransaction().replace(R.id.main_content, detailViewFragment).commit()
        bottom_navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.action_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, detailViewFragment).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.action_search -> {
                    val gridFragment = GridFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, gridFragment).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.action_add_photo -> {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        val uploadFragment = UploadFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.main_content, uploadFragment).commit()
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.action_favorite_alarm -> {
                    val alarmFragment = AlarmFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, alarmFragment).commit()
                    return@setOnItemSelectedListener true
                }
                else -> {  //account
                    val userFragment = UserFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, userFragment).commit()
                    return@setOnItemSelectedListener true
                }
            }
        }
        init()
    }

    private fun init(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            Log.d("TAG", "init: activityResultLauncher ")
            if (it.resultCode == 10){
                Log.d("TAG", "init: activityResultLauncher ")
            }else{
                Log.d("TAG", "init: activityResultLauncher 2")
            }
        }
    }
}
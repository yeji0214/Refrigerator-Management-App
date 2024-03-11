package com.example.refrigerator_management_app_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // ActionBar를 숨깁니다.
        setContentView(R.layout.activity_start)
    }
}

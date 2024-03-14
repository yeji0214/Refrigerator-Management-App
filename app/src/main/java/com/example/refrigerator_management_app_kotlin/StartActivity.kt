package com.example.refrigerator_management_app_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.refrigerator_management_app_kotlin.databinding.ActivityStartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StartActivity : AppCompatActivity() {
    private lateinit var activityStartBinding: ActivityStartBinding // ActivityStartBinding binding

    private lateinit var firebaseAuth: FirebaseAuth // 파이어베이스 인증

    private lateinit var databaseReference: DatabaseReference // 실시간 데이터베이스

    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    // 로그인 액티비티 이동 버튼, 회원가입 액티비티 이동 버튼, 환경설정 액티비티 이동 버튼
    private lateinit var startLoginButton: ImageView
    private lateinit var startJoinButton: ImageView
    private lateinit var startSettingButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // ActionBar를 숨깁니다.
        activityStartBinding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(activityStartBinding.root)

        // 초기화
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        startLoginButton = activityStartBinding.startLoginButton
        startJoinButton = activityStartBinding.startJoinButton
        startSettingButton = activityStartBinding.startSettingButton

        // 로그인 액티비티 이동 버튼 클릭
        startLoginButton.setOnClickListener(View.OnClickListener {
            // 로그인 화면으로 이동
            val intent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(intent)
        })
    }
}

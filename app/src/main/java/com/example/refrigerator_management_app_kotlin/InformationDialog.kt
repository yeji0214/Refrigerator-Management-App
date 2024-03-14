package com.example.refrigerator_management_app_kotlin

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import com.example.refrigerator_management_app_kotlin.databinding.DialogInformationBinding
import com.google.firebase.auth.FirebaseAuth

class InformationDialog(context: Context, private val myMode: Int) : Dialog(context) {
    private lateinit var dialogInformationBinding: DialogInformationBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogInformationBinding = DialogInformationBinding.inflate(layoutInflater)
        setContentView(dialogInformationBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val yesButton = dialogInformationBinding.informationDialogbuttonYes
        val noButton = dialogInformationBinding.informationDialogbuttonNo
        val informationText = dialogInformationBinding.informationDialogText

        when (myMode) {
            // 회원가입 성공시
            1 -> informationText.setImageResource(R.drawable.goto_login)
            // 로그아웃
            2 -> informationText.setImageResource(R.drawable.logout)
            // 앱 종료
            3 -> informationText.setImageResource(R.drawable.finish_application)
            // mode가 설정되어있지 않는 경우
            else -> {
                informationText.setImageResource(R.drawable.empty_text)
                Log.e("InformationDialog의 ", "drawble mode 설정이 되어있지 않음")
            }
        }

        // 버튼 리스너 설정
        yesButton.setOnClickListener {
            Log.e("InformationDialog의 ", "YES 버튼이 눌림")
            when (myMode) {
                // 회원가입 성공시
                // yesButton 누르면 로그인 화면으로 돌아감.
                1 -> {
                    val intent = Intent(context, LoginActivity::class.java)
                    if (context is Activity) {
                        context.startActivity(intent)
                        ActivityCompat.finishAffinity(context as Activity)
                    }
                }
                // 로그아웃
                2 -> {
                    firebaseAuth.signOut()
                    val intent = Intent(context, StartActivity::class.java)
                    if (context is Activity) {
                        context.startActivity(intent)
                        ActivityCompat.finishAffinity(context as Activity)
                        Log.e("로그아웃 성공", "")
                    }
                }
                // 앱 종료
                3 -> {
                    if (context is Activity) {
                        ActivityCompat.finishAffinity(context as Activity)
                    }
                }
            }
            dismiss()
        }

        noButton.setOnClickListener {
            Log.e("InformationDialog의 ", "NO 버튼이 눌림")
            // 회원가입 성공시
            // noButton 누르면 처음 화면으로 돌아감.
            if (myMode == 1) {
                if (context is Activity) {
                    ActivityCompat.finishAffinity(context as Activity)
                }
            }
            dismiss()
        }
    }
}

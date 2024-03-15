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

class InformationDialog(private val activity: Activity, private val myMode: Int) : Dialog(activity) {
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

        // yesButton 클릭 이벤트 핸들러
        yesButton.setOnClickListener {
            Log.e("InformationDialog의 ", "YES 버튼이 눌림")
            when (myMode) {
                1 -> {
                    Log.e("InformationDialog의 ", "회원가입 성공 모드")
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity.startActivity(intent)
                    if (activity !is LoginActivity) {
                        Log.e("InformationDialog의 ", "LoginActivity가 아닙니다. Activity를 종료합니다.")
                        activity.finishAffinity()
                    }
                }
                else -> {
                    Log.e("InformationDialog의 ", "mode가 설정되어있지 않음")
                }
            }
            dismiss()
        }

        noButton.setOnClickListener {
            Log.e("InformationDialog의 ", "NO 버튼이 눌림")
            // 회원가입 성공시
            // noButton 누르면 처음 화면으로 돌아감.
            if (myMode == 1) {
                activity.finish()
            }
            dismiss()
        }
    }
}


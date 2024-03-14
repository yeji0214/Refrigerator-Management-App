package com.example.refrigerator_management_app_kotlin

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.example.refrigerator_management_app_kotlin.databinding.DialogWarningBinding

class WarningDialog(context: Context, private val myMode: Int) : Dialog(context) {
    private lateinit var dialogWarningBinding: DialogWarningBinding // WarningDialog binding
    private lateinit var warningText: ImageView // 다이얼로그의 경고 문구
    private lateinit var okButton: Button // ok 버튼

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogWarningBinding = DialogWarningBinding.inflate(layoutInflater)
        setContentView(dialogWarningBinding.root)

        // 초기화
        warningText = dialogWarningBinding.warningDialogText
        okButton = dialogWarningBinding.warningDialogbuttonOk

        when (myMode) {
            // 이메일과 비밀번호가 비어있는 경우
            1 -> {
                warningText.setImageResource(R.drawable.no_empty_email_password)
                Log.e("WarningDialog의 ", "mode : 1")
            }
            // 비밀번호가 6글자를 넘지 않는 경우
            2 -> {
                warningText.setImageResource(R.drawable.write_six_password)
                Log.e("WarningDialog의 ", "mode : 2")
            }
            // 비밀번호 확인 절차를 진행하지 않은 경우
            3 -> {
                warningText.setImageResource(R.drawable.do_pw_pheck)
                Log.e("WarningDialog의 ", "mode : 3")
            }
            // 회원가입 실패시
            4 -> {
                warningText.setImageResource(R.drawable.retry_register)
                Log.e("WarningDialog의 ", "mode : 4")
            }
            // 냉장고 이름이 비어있는 경우
            5 -> {
                warningText.setImageResource(R.drawable.no_empty_refrigerator)
                Log.e("WarningDialog의 ", "mode : 5")
            }
            // 냉장고 이름이 중복되는 경우
            6 -> {
                warningText.setImageResource(R.drawable.already_exist_id)
                Log.e("WarningDialog의 ", "mode : 6")
            }
            // 로그인에 실패한 경우
            7 -> {
                warningText.setImageResource(R.drawable.login_failed)
                Log.e("WarningDialog의 ", "mode : 7")
            }
            // mode가 설정되어있지 않는 경우
            else -> {
                Log.e("WarningDialog의 ", "mode 설정이 되어있지 않음")
            }
        }

        // okButton 클릭
        okButton.setOnClickListener {
            Log.e("warningDialog의 ", "OK 버튼이 눌림")
            dismiss()
        }
    }
}
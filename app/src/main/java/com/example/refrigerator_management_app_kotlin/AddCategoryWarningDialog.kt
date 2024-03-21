package com.example.refrigerator_management_app_kotlin

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.example.refrigerator_management_app_kotlin.databinding.DialogAddCategoryWarningBinding

class AddCategoryWarningDialog(context: Context, private val mode: Int) : Dialog(context) {
    private lateinit var dialogAddCategoryWarningBinding: DialogAddCategoryWarningBinding
    private lateinit var warningText: ImageView // 다이얼로그의 경고 문구
    private lateinit var okButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogAddCategoryWarningBinding = DialogAddCategoryWarningBinding.inflate(layoutInflater)
        setContentView(dialogAddCategoryWarningBinding.root)

        /* ---------- 초기화 ---------- */
        warningText = dialogAddCategoryWarningBinding.addCategoryWarningDialogText
        okButton = dialogAddCategoryWarningBinding.addCategoryWarningDialogOkBtn

        okButton.setOnClickListener {
            dismiss()
        }

        when (mode) {
            // 카테고리 이름이 비어있는 경우
            1 -> {
                warningText.setImageResource(R.drawable.no_empty_category)
                Log.e("AddCategoryWarningDialog", "카테고리 이름 비어있음")
            }
            // 카테고리 이름이 중복되는 경우
            2 -> {
                warningText.setImageResource(R.drawable.already_exist_name)
                Log.e("AddCategoryWarningDialog", "카테고리 이름 중복")
            }
        }
    }
}

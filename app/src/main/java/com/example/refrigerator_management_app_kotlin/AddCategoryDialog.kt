package com.example.refrigerator_management_app_kotlin

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.refrigerator_management_app_kotlin.databinding.DialogAddCategoryBinding

class AddCategoryDialog(context: Context, private val viewModel: CategoryViewModel, private val type: String) : Dialog(context) {
    private lateinit var dialogAddCategoryBinding: DialogAddCategoryBinding
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private var warningDialog: AddCategoryWarningDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogAddCategoryBinding = DialogAddCategoryBinding.inflate(layoutInflater)
        setContentView(dialogAddCategoryBinding.root)

        okButton = dialogAddCategoryBinding.addCategoryDialogButtonOk
        cancelButton = dialogAddCategoryBinding.addCategoryDialogButtonCancel

        okButton.setOnClickListener {
            val category = dialogAddCategoryBinding.addCategoryDialogEditText.text.toString() // 입력한 카테고리 이름

            // 입력된 카테고리 이름이 비어있는 경우
            if (category.isEmpty()) {
                warningDialog = AddCategoryWarningDialog(context, 1)
                warningDialog?.show()
                return@setOnClickListener
            }
            // 냉장 카테고리 안에 이미 존재하는 경우
            if (type == "냉장" && viewModel.refrigerationCategorys.contains(category)) {
                warningDialog = AddCategoryWarningDialog(context, 2)
                warningDialog?.show()
                return@setOnClickListener
            }
            // 냉동 카테고리 안에 이미 존재하는 경우
            if (type == "냉동" && viewModel.freezeCategorys.contains(category)) {
                warningDialog = AddCategoryWarningDialog(context, 2)
                warningDialog?.show()
                return@setOnClickListener
            }

            // 카테고리 추가
            if (type == "냉장")
                viewModel.addRefrigerationCategory(category)
            else
                viewModel.addFreezeCategory(category)
            Log.e("AddCategoryDialog", "$category 추가")

            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }
    }
}

package com.example.refrigerator_management_app_kotlin

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.refrigerator_management_app_kotlin.databinding.DialogPlusBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddRefrigeratorDialog(context: Context, private val viewModel: RefrigeratorViewModel, private val itemPos: Int, private val userUid: String) : Dialog(context) {
    private lateinit var dialogPlusBinding: DialogPlusBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var editRefrigeratorName: EditText
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private lateinit var warningDialog: WarningDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogPlusBinding = DialogPlusBinding.inflate(layoutInflater)
        setContentView(dialogPlusBinding.root)

        /* ---------- 초기화 ---------- */
        databaseReference = FirebaseDatabase.getInstance().reference
        editRefrigeratorName = dialogPlusBinding.plusDialogEditText
        okButton = dialogPlusBinding.pludDialogbuttonOk
        cancelButton = dialogPlusBinding.plusDialogbuttonCancel

        // 냉장고가 처음 추가되는 것이 아니라면 edit_refrigerator_name에 기존 냉장고의 이름이 전체 선택된 상태로 보여지게 함
        if (itemPos != -1) {
            val name = viewModel.getRefrigeratorName(itemPos)
            editRefrigeratorName.setText(name)
            editRefrigeratorName.selectAll()
        }

        /*
         * ---------- 버튼 리스너 설정 ---------- *
         * itemPos : -1 = 새로운 냉장고 추가, else = 기존 냉장고의 수정 및 삭제 (수정은 아직 미구현)
         */
        okButton.setOnClickListener {
            // 냉장고 이름이 비어있는 경우 추가 불가
            if (editRefrigeratorName.text.toString().isEmpty()) {
                warningDialog = WarningDialog(context, 5)
                warningDialog.show()
                return@setOnClickListener
            }
            // 냉장고 이름이 중복되는 경우 추가 불가
            else if (viewModel.refrigerators.contains(editRefrigeratorName.text.toString())) {
                warningDialog = WarningDialog(context, 6)
                warningDialog.show()
                return@setOnClickListener
            }
            // 냉장고 추가
            else {
                Log.e("AddRefrigeratorDialog", "${editRefrigeratorName.text} 추가")

                // 새로운 냉장고 추가
                if (itemPos == -1) {
                    // 파이어베이스에 냉장고 추가
                    // 파이어베이스에 추가하면 자동으로 뷰모델에 추가되어 사용자 화면에 보여짐
                    databaseReference.child("UserAccount").child(userUid).child("냉장고")
                        .child(editRefrigeratorName.text.toString()).setValue("")

                    // 냉장, 냉동까지 생성
//                    databaseReference.child("UserAccount").child(userUid).child("냉장고")
//                        .child(editRefrigeratorName.text.toString()).child("냉장").setValue("")
//                    databaseReference.child("UserAccount").child(userUid).child("냉장고")
//                        .child(editRefrigeratorName.text.toString()).child("냉동").setValue("")
                } else {
                    val refrigeratorPastName = viewModel.getRefrigeratorName(itemPos) // 냉장고의 전 이름
                    val refrigeratorNewName = editRefrigeratorName.text.toString() // 냉장고의 수정된 이름
                    viewModel.deleteRefrigerator(itemPos) // 뷰모델에서 기존 냉장고 삭제
                    val updateDatabaseReference = FirebaseDatabase.getInstance().reference
                    val newPath = updateDatabaseReference.child("UserAccount").child(userUid).child("냉장고")
                        .child(refrigeratorNewName) // 냉장고의 수정된 이름으로 DatabaseReference를 만듦
                    val pastPath = databaseReference.child("UserAccount").child(userUid).child("냉장고")
                        .child(refrigeratorPastName.toString()) // 냉장고의 수정 전 이름의 DatabaseReference를 가져옴
                    // pastPath에서 newPath로 데이터 이동, 파이어베이스에서 냉장고의 이름이 수정되는 것 처럼 작동함
                    // 이때 newPath의 DatabaseReference에 pastPath의 데이터가 추가되면서
                    // 자동으로 뷰모델의 addRefrigerator가 호출되어 사용자의 화면에도 냉장고의 이름이 수정되어 나타남
                    updateRefrigerator(newPath, pastPath)
                    pastPath.removeValue() // 수정 전 냉장고는 삭제함
                }
            }
            dismiss()
        }

        cancelButton.setOnClickListener { dismiss() }
    }

    // 파이어베이스 안에서의 냉장고 DatabaseReference 이동
    private fun updateRefrigerator(newPath: DatabaseReference, pastPath: DatabaseReference) {
        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                newPath.setValue(snapshot.value).addOnCompleteListener { task ->
                    // 성공
                    if (task.isComplete) Log.d("updateRefrigerator", "Success update Users")
                    // 실패
                    else Log.d("updateRefrigerator", "Fail update Users")
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        pastPath.addListenerForSingleValueEvent(valueEventListener)
    }
}

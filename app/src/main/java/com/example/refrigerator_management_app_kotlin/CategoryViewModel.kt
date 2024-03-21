package com.example.refrigerator_management_app_kotlin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class CategoryViewModel : ViewModel() {
    val refrigerationCategorysLiveData = MutableLiveData<ArrayList<String>>() // "냉장" 카테고리 live data
    val refrigerationCategorys = ArrayList<String>() // "냉장" 카테고리 array list

    val freezeCategorysLiveData = MutableLiveData<ArrayList<String>>() // "냉동" 카테고리 live data
    val freezeCategorys = ArrayList<String>() // "냉동" 카테고리 array list

    var longClickPosition: Int = 0

    private var databaseCategoryReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var userUid: String = ""
    private var refrigeratorId: String = ""

    init {
        databaseCategoryReference = FirebaseDatabase.getInstance().reference
    }

    fun addRefrigerationCategory(category: String) {
        if (refrigerationCategorys.contains(category)) // 이미 같은 카테고리를 가지고 있으면 X
            return

        Log.e("CategoryViewModel", "카테고리 $category 추가")
        refrigerationCategorys.add(category)
        createRefrigerationCategoryInDatabase(category) // 데이터베이스에도 추가
        refrigerationCategorys.sort() // 정렬
        refrigerationCategorysLiveData.value = refrigerationCategorys // 옵저버에게 라이브데이터가 변경된 것을 알리기 위해
    }

    fun addFreezeCategory(category: String) {
        if (freezeCategorys.contains(category)) // 이미 같은 카테고리를 가지고 있으면 X
            return

        freezeCategorys.add(category)
        createFreezeCategoryInDatabase(category) // 데이터베이스에도 추가
        freezeCategorys.sort() // 정렬
        freezeCategorysLiveData.value = freezeCategorys // 옵저버에게 라이브데이터가 변경된 것을 알리기 위해
    }

    fun deleteRefrigerationCategory(position: Int) {
        val temp = refrigerationCategorys[position]
        refrigerationCategorys.removeAt(position) // 뷰모델에서 삭제
        deleteRefrigerationCategoryInDatabase(temp) // 데이터베이스에서 삭제
        refrigerationCategorysLiveData.value = refrigerationCategorys // 옵저버에서 라이브데이터가 변경된 것을 알리기 위해
    }

    fun deleteFreezeCategory(position: Int) {
        val temp = freezeCategorys[position]
        freezeCategorys.removeAt(position) // 뷰모델에서 삭제
        deleteFreezeCategoryInDatabase(temp) // 데이터베이스에서 삭제
        freezeCategorysLiveData.value = freezeCategorys // 옵저버에게 라이브데이터가 변경된 것을 알리기 위해
    }

    fun getRefrigerationCategorySize(): Int {
        return refrigerationCategorys.size
    }

    fun getFreezeCategorySize(): Int {
        return freezeCategorys.size
    }

    /* ---------- get set ---------- */
    fun getUserUid(): String {
        return userUid
    }

    fun setUserUid(userUid: String) {
        this.userUid = userUid
    }

    fun getRefrigeratorId(): String {
        return refrigeratorId
    }

    fun setRefrigeratorId(refrigeratorId: String) {
        this.refrigeratorId = refrigeratorId
    }

    /* ---------- 데이터베이스 ---------- */
    // "냉장" 아래 카테고리 추가
    private fun createRefrigerationCategoryInDatabase(category: String) {
        // 카테고리를 처음 만들었을 때 카테고리 안에 아무것도 들어있지 않기 때문에 데이터베이스에 제대로 카테고리가 만들어지지 않음
        // 그래서 카테고리 아래에 temp라는 임시 폴더를 만들어두었고, 액티비티에는 안 보이도록 함 (데이터베이스에서 보면 카테고리 아래에 temp가 존재)
        databaseCategoryReference.child("UserAccount").child(userUid).child("냉장고").child(refrigeratorId).child("냉장").child(category).child("temp").push().setValue("")
    }

    // "냉동" 아래 카테고리 추가
    private fun createFreezeCategoryInDatabase(category: String) {
        databaseCategoryReference.child("UserAccount").child(userUid).child("냉장고").child(refrigeratorId).child("냉동").child(category).child("temp").push().setValue("")
    }

    private fun deleteRefrigerationCategoryInDatabase(category: String) {
        databaseCategoryReference.child("UserAccount").child(userUid).child("냉장고").child(refrigeratorId).child("냉장").child(category).removeValue()
    }

    private fun deleteFreezeCategoryInDatabase(category: String) {
        databaseCategoryReference.child("UserAccount").child(userUid).child("냉장고").child(refrigeratorId).child("냉동").child(category).removeValue()
    }
}

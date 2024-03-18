package com.example.refrigerator_management_app_kotlin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RefrigeratorViewModel : ViewModel() {
    private val _refrigeratorsLiveData = MutableLiveData<ArrayList<String>>()
    val refrigeratorsLiveData: MutableLiveData<ArrayList<String>>
        get() = _refrigeratorsLiveData

    val refrigerators = ArrayList<String>()

    var refrigeratorPosition = 0 // 냉장고 위치

    var userUid: String? = null
    private var refrigeratorName: String? = null

    // 냉장고 추가
    fun addRefrigerator(name: String) {
        refrigerators.add(name) // 냉장고 리스트에 냉장고 추가
        refrigerators.sort() // 오름차순으로 정렬
        _refrigeratorsLiveData.value = refrigerators

        Log.e("RefrigeratorViewModel", "$name 냉장고 추가")
    }

    // 냉장고 삭제
    fun deleteRefrigerator(pos: Int) {
        val name = refrigerators[pos] // 클릭한 냉장고의 이름
        refrigerators.removeAt(pos) // 냉장고 리스트에서 클릭한 냉장고 삭제
        refrigerators.sort() // 오름차순으로 정렬
        _refrigeratorsLiveData.value = refrigerators

        Log.e("RefrigeratorViewModel", "$name 냉장고 삭제")
    }

    // 냉장고 위치 설정
    fun setPosition(position: Int) {
        refrigeratorPosition = position
    }

    // 사용자 UID 설정
    fun setUserId(uid: String) {
        userUid = uid
    }

    /* ---------- getter setter ---------- */
    val refrigeratorsSize: Int
        get() = refrigerators.size

    fun getRefrigeratorName(pos: Int): String? {
        refrigeratorName = refrigerators.getOrNull(pos)
        return refrigeratorName
    }

    fun getPosition(): Int {
        return refrigeratorPosition
    }
}

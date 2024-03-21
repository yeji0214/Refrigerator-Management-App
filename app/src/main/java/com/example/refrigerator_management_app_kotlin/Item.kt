package com.example.refrigerator_management_app_kotlin

import android.graphics.Bitmap

class Item {
    var name: String // 이름
    var expirationDate: String // 유통기한
    var memo: String // 메모
    var remainDays = 0 // 남은 기간
    var bitmap: Bitmap? = null

    constructor() {
        name = "초기화 x"
        expirationDate = "초기화 x"
        memo = "초기화 x"
    }

    constructor(name: String, expirationDate: String, memo: String) {
        this.name = name
        this.expirationDate = expirationDate
        this.memo = memo
    }

    override fun equals(other: Any?): Boolean {
        var same = false
        if (other != null && other is Item) {
            same = this.name == other.name
        }
        return same
    }

    override fun hashCode(): Int {
        return name.hashCode() + expirationDate.hashCode() + memo.hashCode()
    }
}

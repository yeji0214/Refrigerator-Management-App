package com.example.refrigerator_management_app_kotlin

class UserAccount {
    private var idToken: String? = null // firebase Uid (한 사용자의 고유 정보)
    private var userId: String? = null // 아이디
    private var userEmail: String? = null // 이메일 아이디
    private var userPw: String? = null // 비밀번호

    // firebase에서는 기본 생성자를 만들지 않으면(빈 생성자여도) 데이터 read시 오류가 날 수 있음.
    constructor()

    fun setIdToken(idToken: String?) {
        this.idToken = idToken
    }


    fun setUserId(userId: String?) {
        this.userId = userId
    }

    fun setUserEmail(userEmail: String?) {
        this.userEmail = userEmail
    }

    fun setUserPw(userPw: String?) {
        this.userPw = userPw
    }
    fun getIdToken(): String? {
        return idToken
    }

    fun getUserId(): String? {
        return userId
    }

    fun getUserEmail(): String? {
        return userEmail
    }

    fun getUserPw(): String? {
        return userPw
    }
}

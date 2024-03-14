package com.example.refrigerator_management_app_kotlin

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.refrigerator_management_app_kotlin.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class JoinActivity : AppCompatActivity() {
    private lateinit var activityJoinBinding: ActivityJoinBinding // RegisterActivity binding
    //private lateinit var warningDialog: WarningDialog // 경고 다이얼로그
    //private lateinit var informationDialog: InformationDialog // 안내 다이얼로그
    private lateinit var firebaseAuth: FirebaseAuth // 파이어베이스 인증
    private lateinit var databaseReference: DatabaseReference // 실시간 데이터베이스
    private lateinit var joinPwCheckWarningImage: ImageView // 회원가입 비밀번호 확인 오류 이미지
    private lateinit var joinPwCheckWarningText: TextView // 회원가입 비밀번호 확인 오류 텍스트
    private lateinit var joinID: EditText // 회원가입 입력 필드 (아이디, 이메일, 비밀번호, 비밀번호 확인)
    private lateinit var joinEmail: EditText
    private lateinit var joinPw: EditText
    private lateinit var joinPwCheck: EditText
    private lateinit var joinButton: ImageView // 회원가입 버튼
    private var joinPwCheckFlag = 0 // 회원가입 확인 절차 진행 여부
    private lateinit var textWatcher: TextWatcher // 텍스트 변경 감지

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // ActionBar를 숨깁니다.
        activityJoinBinding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(activityJoinBinding.root)

        // 초기화
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        joinID = activityJoinBinding.joinIdText
        joinEmail = activityJoinBinding.joinEmailText
        joinPw = activityJoinBinding.joinPwText
        joinPwCheck = activityJoinBinding.joinPwCheckText
        joinButton = activityJoinBinding.joinButton
        joinPwCheckWarningImage = activityJoinBinding.joinPwCheckWarningImage
        joinPwCheckWarningText = activityJoinBinding.joinPwCheckWarningText

        joinPwCheckFlag = 0 // 0 = 비밀번호가 비밀번호 확인과 같지 않음, 1 = 비밀번호가 비밀번호 확인과 같음.

        // 텍스트 변경 감지
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            // 텍스트가 변경된 후
            override fun afterTextChanged(editable: Editable) {
                // 비밀번호가 비밀번호 확인이랑 같은 경우
                if (joinPw.text.toString() == joinPwCheck.text.toString()) {
                    // 회원가입 확인 절차 진행 여부 flag 1로 변경.
                    joinPwCheckFlag = 1
                    // 경고 이미지 체크 표시로 변경.
                    joinPwCheckWarningImage.setImageResource(R.drawable.check)
                    // 경고 이미지 배경색 투명색으로 변경.
                    joinPwCheckWarningImage.setBackgroundColor(Color.parseColor("#e6f0ff"))
                    // 경고 문구 변경.
                    joinPwCheckWarningText.text = "비밀번호가 맞았습니다!"
                    // 경고 문구색 초록색으로 변경.
                    joinPwCheckWarningText.setTextColor(Color.parseColor("#4CAF50"))
                    Log.e("password와 passwordCheck가 일치함", "${joinPw.text} ${joinPwCheck.text}")
                } else {
                    // 회원가입 확인 절차 진행 여부 flag 0으로 변경.
                    joinPwCheckFlag = 0
                    // 경고 이미지 경고 표시로 변경.
                    joinPwCheckWarningImage.setImageResource(android.R.drawable.stat_sys_warning)
                    // 경고 이미지 배경색 빨간색으로 변경.
                    joinPwCheckWarningImage.setBackgroundColor(Color.parseColor("#F44336"))
                    // 경고 문구 변경.
                    joinPwCheckWarningText.text = "비밀번호가 맞지 않습니다!"
                    // 경고 문구색 빨간색으로 변경.
                    joinPwCheckWarningText.setTextColor(Color.parseColor("#F44336"))
                    Log.e("password와 passwordCheck가 일치하지 않음", "${joinPw.text} ${joinPwCheck.text}")
                }
            }
        }

        // 비밀번호 입력 필드와 비밀번호 확인 입력 필드 모두에 텍스트 감지 설정.
        // 둘 중 아무 입력 필드에 입력해도 텍스트 감지하여 일치 여부 확인 가능하도록 함.
        joinPw.addTextChangedListener(textWatcher)
        joinPwCheck.addTextChangedListener(textWatcher)

        // 회원가입 버튼 클릭 후 회원가입 처리 시작
        joinButton.setOnClickListener {
            // 사용자가 입력한 Id, Email, Pw
            val id = joinID.text.toString()
            val email = joinEmail.text.toString()
            val password = joinPw.text.toString()

            // 이메일과 비밀번호가 비어있을 경우 경고
            if (email.isEmpty() && password.isEmpty()) {
//                warningDialog = WarningDialog(this@JoinActivity, 1)
//                warningDialog.show()
            } else if (password.length < 6) { // 비밀번호가 6글자 이상 넘지 않으면 경고
//                warningDialog = WarningDialog(this@JoinActivity, 2)
//                warningDialog.show()
            } else if (joinPwCheckFlag == 0) { // 비밀번호 확인 절차를 진행하지 않은 경우
//                warningDialog = WarningDialog(this@JoinActivity, 3)
//                warningDialog.show()
            } else {
                // firebase에 이메일과 비밀번호를 이용하여 사용자 계정 생성 시작
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@JoinActivity) { task ->
                        // task는 실제로 회원가입 처리를 한 후의 결과값이다.
                        // 성공시
                        if (task.isSuccessful) {
                            // 방금 회원가입 처리가 된 user를 가져온다.
                            val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                            val userAccount = UserAccount()
                            // userAccount에 방금 회원가입된 user의 정보들을 설정한다. (Id, Email, Password)
//                            firebaseUser?.uid?.let { userAccount.idToken = it }
//                            userAccount.userId = id
//                            firebaseUser?.email?.let { userAccount.userEmail = it }
//                            userAccount.userPw = password
                            userAccount.setIdToken((firebaseUser!!.uid))
                            userAccount.setUserId(id)
                            userAccount.setUserEmail(firebaseUser.email)
                            userAccount.setUserPw(password)

                            // 생성된 userAccount를 user의 userUid의 child로 database에 삽입.
                            // 회원가입 시 사용자가 입력한 id의 child로 삽입하는 방법도 생각해보기
                            databaseReference.child("UserAccount").child(firebaseUser.uid)
                                .setValue(userAccount)

                            // 회원가입 성공 다이얼로그
//                            informationDialog = InformationDialog(this@JoinActivity, 1)
//                            informationDialog.show()

                            Log.e(
                                "회원가입 성공",
                                "${databaseReference} ${userAccount.getIdToken()} ${userAccount.getUserId()} ${userAccount.getUserEmail()} ${userAccount.getUserPw()}"
                            )
                        } else {
                            // 회원가입 실패 다이얼로그
//                            warningDialog = WarningDialog(this@JoinActivity, 4)
//                            warningDialog.show()

                            Log.e("회원가입 실패", "")
                        }
                    }
            }
        }
    }
}

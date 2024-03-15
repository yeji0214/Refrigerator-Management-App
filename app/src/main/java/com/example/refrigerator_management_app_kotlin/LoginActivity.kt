package com.example.refrigerator_management_app_kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.refrigerator_management_app_kotlin.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var warningDialog: WarningDialog
    private lateinit var firstLoginEmail: EditText
    private lateinit var firstLoginPw: EditText
    private lateinit var loginButton: ImageView
    private lateinit var gotoJoinButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // ActionBar를 숨깁니다.

        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        firstLoginEmail = activityLoginBinding.loginEmailText
        firstLoginPw = activityLoginBinding.loginPwText
        loginButton = activityLoginBinding.loginButton
        gotoJoinButton = activityLoginBinding.loginGoToJoinButton

        loginButton.setOnClickListener {
            val email = firstLoginEmail.text.toString()
            val password = firstLoginPw.text.toString()
            Log.e("LoginActivity", "email = $email, password = $password")

            if (email.isEmpty() || password.isEmpty()) {
                warningDialog = WarningDialog(this@LoginActivity, 1)
                warningDialog.show()
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this@LoginActivity, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                        val userUid = firebaseUser?.uid
                        databaseReference.child("UserAccount").child(userUid!!).child("userId").get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userUid = firebaseUser.uid
                                val userId = task.result?.value as String
                                val intent = Intent(this@LoginActivity, RefrigeratorActivity::class.java)
                                intent.putExtra("userUid", userUid)
                                intent.putExtra("userID", userId)
                                Log.e("Login userUid : ", userUid)
                                Log.e("Login userId : ", userId)
                                Log.e("로그인 성공", "Id : $userId email : $email password : $password")
                                startActivity(intent)
                                finish()
                            }
                        }

                    } else {
                        warningDialog = WarningDialog(this@LoginActivity, 7)
                        warningDialog.show()
                        Log.e("로그인 실패", "")
                    }
                })
            }
        }

        gotoJoinButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, JoinActivity::class.java)
            startActivity(intent)
        }
    }
}

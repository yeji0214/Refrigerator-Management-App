package com.example.refrigerator_management_app_kotlin

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

class CategoryActivity : AppCompatActivity() {
    private val refrigerationCategoryFragment = 1
    private val freezeCategoryFragment = 2

    private lateinit var refrigerationImage: ImageView
    private lateinit var freezeImage: ImageView
    private lateinit var settingIcon: ImageView
    private lateinit var toRefrigeratorActivityBtn: ImageView
    private lateinit var refrigeratorIdTextView: TextView

    private var userUid: String? = null
    private var refrigeratorId: String? = null
    private var type = "냉장" // 냉장 프래그먼트로 시작하므로 "냉장"으로 초기화

    //private lateinit var viewModel: CategoryViewModel

    private lateinit var categoryPreferences: SharedPreferences
    private val SharedPrefFile = "com.example.android.MyApplication3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // ActionBar를 숨깁니다.
        setContentView(R.layout.activity_category)

        /* ---------- 초기화 ---------- */
        refrigerationImage = findViewById(R.id.refrigerationImage)
        freezeImage = findViewById(R.id.freezeImage)
        settingIcon = findViewById(R.id.settingIcon)
        toRefrigeratorActivityBtn = findViewById(R.id.toRefrigeratorActivity)
        refrigeratorIdTextView = findViewById(R.id.refrigeratorIdTextView)

        /* ---------- 리스너 ---------- */
        refrigerationImage.setOnClickListener { refrigerationImageClick(it) }
        freezeImage.setOnClickListener { freezeImageClick(it) }
        settingIcon.setOnClickListener { settingIconClick(it) }
        toRefrigeratorActivityBtn.setOnClickListener { toRefrigeratorActivity(it) }

        /* ---------- Intent 받아와서 uid, id 초기화 ---------- */
        intent?.run {
            userUid = getStringExtra("userUid") // 냉장고 Uid
            refrigeratorId = getStringExtra("refrigeratorID") // 냉장고 id
        }

        /* ---------- uid, id가 null이면 저장소에서 값 가져오기 ---------- */
        categoryPreferences = getSharedPreferences(SharedPrefFile, MODE_PRIVATE)
        userUid = userUid ?: categoryPreferences.getString("userUid", null)
        refrigeratorId = refrigeratorId ?: categoryPreferences.getString("ID", null)
        refrigeratorIdTextView.text = refrigeratorId

        /* ---------- View Model ---------- */
//        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
//        viewModel.setUserUid(userUid)
//        viewModel.setRefrigeratorId(refrigeratorId)

        // 리싸이클러뷰, 어댑터는 각 프래그먼트에서 생성
    }

    override fun onPause() {
        super.onPause()
        val preferencesEditor = categoryPreferences.edit()

        preferencesEditor.putString("userUid", userUid)
        preferencesEditor.putString("ID", refrigeratorId)

        preferencesEditor.apply()
    }

//    override fun onCreateContextMenu(contextMenu: ContextMenu, view: View, contextMenuInfo: ContextMenu.ContextMenuInfo) {
//        (view.context as? Activity)?.menuInflater?.inflate(R.menu.category_context_menu, contextMenu)
//        Log.e("MainActivity", "onCreateContextMenu()")
//    }

    fun getUserUid(): String? = userUid
    fun getRefrigeratorId(): String? = refrigeratorId
    //fun getViewModel(): CategoryViewModel = viewModel

    // 인자로 받은 fragment로 이동하는 함수
    fun changeFragment(fragment: Int) {
        val transaction = supportFragmentManager.beginTransaction()

//        when (fragment) {
//            1 -> { // RefrigerationCategoryFragment 호출
//                val refrigerationCategoryFrag = RefrigerationCategoryFragment()
//                transaction.replace(R.id.categoryFragmentContainerView, refrigerationCategoryFrag)
//                transaction.commit()
//            }
//            2 -> { // FreezeCategoryFragment 호출
//                val freezeCategoryFrag = FreezeCategoryFragment()
//                transaction.replace(R.id.categoryFragmentContainerView, freezeCategoryFrag)
//                transaction.commit()
//            }
//        }
    }

    // "냉장" 클릭 -> refrigerationCategoryFragment로 전환, image 바꾸기
    fun refrigerationImageClick(v: View) {
        Log.e("CategoryActivity", "냉장 버튼 Click")
        type = "냉장"

        refrigerationImage.setImageResource(R.drawable.selected_refrigeration_btn)
        freezeImage.setImageResource(R.drawable.unselected_freeze_btn)

        changeFragment(refrigerationCategoryFragment)
    }

    // "냉동" 클릭 -> freezeCategoryFragment로 전환, image 바꾸기
    fun freezeImageClick(v: View) {
        Log.e("CategoryActivity", "냉동 버튼 Click")
        type = "냉동"

        refrigerationImage.setImageResource(R.drawable.unselected_refrigeration_btn)
        freezeImage.setImageResource(R.drawable.selected_freeze_btn)

        changeFragment(freezeCategoryFragment)
    }

    // "설정" 아이콘 클릭 -> SettingsActivity 이동
    fun settingIconClick(v: View) {
//        val intent = Intent(this, SettingsActivity::class.java)
//        startActivity(intent)
    }

    // <- 아이콘 클릭 -> RefrigeratorActivity 이동
    fun toRefrigeratorActivity(v: View) {
        val intent = Intent(this, RefrigeratorActivity::class.java)
        intent.putExtra("userUid", userUid)
        startActivity(intent)
    }
}

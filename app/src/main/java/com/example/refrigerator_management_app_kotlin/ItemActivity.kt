package com.example.refrigerator_management_app_kotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

class ItemActivity : AppCompatActivity() {
    private val itemFragment = 1
    private val itemInformationFragment = 2
    private val addItemFragment = 3
    private val calendarFragment = 4
    private var currentFragment = itemFragment

    private var userUid: String? = null
    private var refrigeratorId: String? = null
    private var category: String? = null
    private var type: String? = null
    private var clickItemName: String? = null
    private lateinit var selectedCategoryTextView: TextView

//    private val viewModel: ItemViewModel by viewModels()
//    private val allItems = ArrayList<Item>()

    private lateinit var itemPreferences: SharedPreferences
    private val sharedPrefFile = "com.example.android.MyApplication3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // ActionBar를 숨깁니다.
        setContentView(R.layout.activity_item)

        Log.e("ItemActivity", "onCreate()")

        /* ---------- Intent 받아와서 uid, id, category 초기화 ---------- */
        intent?.let {
            userUid = it.getStringExtra("userUid")
            refrigeratorId = it.getStringExtra("ID")
            category = it.getStringExtra("category")
            type = it.getStringExtra("type")
        }

        /* ---------- uid, id, category, type이 null이면 저장소에서 값 가져오기 ---------- */
        itemPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
        if (userUid == null || refrigeratorId == null || category == null || type == null) {
            userUid = itemPreferences.getString("userUid", null)
            refrigeratorId = itemPreferences.getString("ID", null)
            category = itemPreferences.getString("category", null)
            type = itemPreferences.getString("type", null)
        }
        selectedCategoryTextView = findViewById(R.id.selectedCategoryTextView)
        selectedCategoryTextView.text = "$type $category"
    }

    override fun onStart() {
        super.onStart()
        changeFragment(currentFragment)
    }

    override fun onPause() {
        super.onPause()
        val preferencesEditor = itemPreferences.edit()

        preferencesEditor.putString("userUid", userUid)
        preferencesEditor.putString("ID", refrigeratorId)
        preferencesEditor.putString("category", category)
        preferencesEditor.putString("type", type)

        preferencesEditor.apply()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //menuInflater.inflate(R.menu.item_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return super.onContextItemSelected(item)
    }

    fun getUserUid(): String? = userUid
    fun getRefrigeratorId(): String? = refrigeratorId
    fun getCategory(): String? = category
    fun getType(): String? = type
//    fun getViewModel(): ItemViewModel = viewModel
//    fun getAllItems(): ArrayList<Item> = allItems
    fun getClickItemName(): String? = clickItemName
    fun setClickItemName(clickItemName: String) {
        this.clickItemName = clickItemName
    }

    // 인자로 받은 fragment로 이동하는 함수
    fun changeFragment(fragment: Int) {
        val transaction = supportFragmentManager.beginTransaction()

        when (fragment) {
            itemFragment -> {
                //val itemFrag = ItemFragment()
                //transaction.replace(R.id.fragmentContainerView, itemFrag)
            }
            itemInformationFragment -> {
//                val itemInfoFrag = ItemInformationFragment(clickItemName)
//                transaction.replace(R.id.fragmentContainerView, itemInfoFrag)
            }
            addItemFragment -> {
//                val addItemFrag = AddItemFragment()
//                transaction.replace(R.id.fragmentContainerView, addItemFrag)
            }
            calendarFragment -> {
//                val calendarFrag = CalendarFragment(this)
//                transaction.replace(R.id.fragmentContainerView, calendarFrag)
            }
        }
        transaction.commit()
    }

    // 아이템 정보 업데이트하는 함수
    fun updateItemInformation() {
//        val nameEditText = findViewById<EditText>(R.id.nameEditText)
//        val expirationDateEditText = findViewById<EditText>(R.id.expirationDateEditText)
//        val memoEditText = findViewById<EditText>(R.id.memoEditText)

//        val name = nameEditText.text.toString()
//        val expirationDate = expirationDateEditText.text.toString()
//        val memo = memoEditText.text.toString()
//
//        val item = Item(name, expirationDate, memo)
//        val index = viewModel.getItemIndex(clickItemName)
//        viewModel.updateItem(index, item)
    }

    /*
     * ---------- ItemFragment layout method ---------- *
     */
    // + 버튼 클릭 -> AddItemFragment 이동
    fun addItemFloatingButtonClick(v: View) {
        changeFragment(addItemFragment)
    }

    // 아이템 클릭 -> ItemInformationFragment 이동, 클릭한 아이템 알려줘야함
    fun itemClick(v: View) {
        val textView = v as TextView
        Log.e("ItemAcitivity", "${textView.text} Click")

        clickItemName = textView.text.toString()
        changeFragment(itemInformationFragment)
    }

    // <- 버튼 클릭 -> CategoryActivity 이동
    fun backImageClick(v: View) {
        val intent = Intent(this, CategoryActivity::class.java)
        startActivity(intent)
    }

    /*
     * ---------- ItemInformationFragment layout method ---------- *
     */
    // "완료" 버튼 클릭 -> 수정된 정보 업데이트하고, itemFragment로 이동
    fun finishButtonClick(v: View) {
        updateItemInformation()
        changeFragment(itemFragment)
    }

    /*
     * ---------- CalendarFragment layout method ---------- *
     */
    fun calendarImageClick(v: View) {
        changeFragment(calendarFragment)
    }
}

package com.example.refrigerator_management_app_kotlin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.Comparator

class ItemViewModel : ViewModel() {
    val itemsLiveData = MutableLiveData<ArrayList<Item>>()
    val items = ArrayList<Item>()

    var longClickPosition: Int = 0

    private var databaseItemReference: DatabaseReference? = null
    private var userUid: String? = null
    private var refrigeratorId: String? = null
    private var category: String? = null
    private var type: String? = null

    init {
        databaseItemReference = FirebaseDatabase.getInstance().reference
    }

    fun addItem(item: Item) {
        if (items.contains(item))
            return

        items.add(item)
        createItemInDatabase(item)
        Collections.sort(items, ItemNameComparator())

        if (items.size == 1)
            deleteTempCategoryInDatabase()

        itemsLiveData.value = items
    }

    fun deleteItem(position: Int) {
        Log.e("ItemViewModel", "deleteItem(), position = $position")
        val temp = items[position]
        items.removeAt(position)
        deleteItemInDatabase(temp)
        itemsLiveData.value = items
    }

    fun deleteItem(item: Item) {
        val position = getItemIndex(item.name)
        deleteItem(position)
    }

    fun updateItem(index: Int, item: Item) {
        updateItemInDatabase(items[index], item)
        items[index] = item
        Collections.sort(items, ItemNameComparator())
        itemsLiveData.value = items
    }

    fun getItemIndex(itemName: String): Int {
        for (i in items.indices) {
            val temp = items[i]
            if (itemName == temp.name)
                return i
        }
        return -1
    }

    fun initItems() {
        items.clear()
        itemsLiveData.value = items
    }

    fun getUserUid(): String? = userUid
    fun setUserUid(userUid: String) { this.userUid = userUid }
    fun getRefrigeratorId(): String? = refrigeratorId
    fun setRefrigeratorId(refrigeratorId: String) { this.refrigeratorId = refrigeratorId }
    fun getCategory(): String? = category
    fun setCategory(category: String) { this.category = category }
    fun getType(): String? = type
    fun setType(type: String) { this.type = type }
    fun getItemSize(): Int = items.size

    private fun createItemInDatabase(item: Item) {
        Log.e("ItemViewModel", "${item.name} 데이터 베이스에 추가")
        databaseItemReference = FirebaseDatabase.getInstance().reference
        val itemReference = databaseItemReference?.child("UserAccount")?.child(userUid!!)?.child("냉장고")?.child(refrigeratorId!!)?.child(type!!)?.child(category!!)?.child(item.name)
        itemReference?.child("이름")?.setValue(item.name)
        itemReference?.child("유통기한")?.setValue(item.expirationDate)
        itemReference?.child("메모")?.setValue(item.memo)
    }

    private fun deleteItemInDatabase(item: Item) {
        databaseItemReference = FirebaseDatabase.getInstance().reference.child("UserAccount").child(userUid!!).child("냉장고").child(refrigeratorId!!).child(type!!).child(category!!)
        databaseItemReference?.child(item.name)?.removeValue()
    }

    private fun updateItemInDatabase(item1: Item, item2: Item) {
        deleteItemInDatabase(item1)
        createItemInDatabase(item2)
    }

    private fun deleteTempCategoryInDatabase() {
        databaseItemReference?.child("UserAccount")?.child(userUid!!)?.child("냉장고")?.child(refrigeratorId!!)?.child(type!!)?.child(category!!)?.child("temp")?.removeValue()
    }

    inner class ItemNameComparator : Comparator<Item> {
        override fun compare(item1: Item, item2: Item): Int {
            val i = item1.name.compareTo(item2.name)
            return when {
                i > 0 -> 1
                i < 0 -> -1
                else -> 0
            }
        }
    }
}

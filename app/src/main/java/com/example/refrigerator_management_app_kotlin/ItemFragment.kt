package com.example.refrigerator_management_app_kotlin

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.refrigerator_management_app_kotlin.ItemActivity
import com.example.refrigerator_management_app_kotlin.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class ItemFragment : Fragment() {
    private var userUid: String? = null
    private var refrigeratorId: String? = null
    private var category: String? = null
    private var type: String? = null

    private lateinit var viewModel: ItemViewModel
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var allItems: ArrayList<Item>

    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* ---------- ItemActivity에서 uid, id, category 가져오기 ---------- */
        userUid = (activity as? ItemActivity)?.getUserUid()
        refrigeratorId = (activity as? ItemActivity)?.getRefrigeratorId()
        category = (activity as? ItemActivity)?.getCategory()
        type = (activity as? ItemActivity)?.getType()

        Log.e("ItemFragment", "category = $category")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allItems = (activity as? ItemActivity)?.getAllItems() ?: ArrayList()

        /* ---------- View Model ---------- */
        viewModel = (activity as? ItemActivity)?.fetchViewModel() ?: ItemViewModel()

        /* ---------- Recycler View ---------- */
        itemRecyclerView = view.findViewById(R.id.itemRecyclerView)
        itemAdapter = ItemAdapter(viewModel)
        itemRecyclerView.adapter = itemAdapter
        itemRecyclerView.layoutManager = GridLayoutManager(context, 2)
        itemRecyclerView.setHasFixedSize(true)

        registerForContextMenu(itemRecyclerView)

        val observer = Observer<ArrayList<Item>> { items ->
            itemAdapter.notifyDataSetChanged() // 어댑터에게 데이터가 변경되었다는 것을 알리기 위해
        }
        viewModel.itemsLiveData.observe(viewLifecycleOwner, observer)
    }

    override fun onStart() {
        super.onStart()
        showItemList()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        val inflater = activity?.menuInflater
        inflater?.inflate(R.menu.item_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.deleteItem -> {
                // 아이템 삭제
                viewModel.deleteItem(viewModel.longClickPosition)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun showItemList() {
        Log.e("ItemFragment", "viewModel size = ${viewModel.getItemSize()}")
        FirebaseDatabase.getInstance().getReference().child("UserAccount").child(userUid.orEmpty())
            .child("냉장고").child(refrigeratorId.orEmpty()).child(type.orEmpty()).child(category.orEmpty())
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    // 해당 카테고리 아래에 새로운 아이템이 추가된 경우 뷰모델에 추가
                    val value = snapshot.getValue(Object::class.java)?.toString()
                    if (value == null || value.startsWith("{-")) return

                    val st = StringTokenizer(value, ",")

                    if (st.countTokens() > 1 && value != "temp") {
                        val memo = st.nextToken().substring(4)
                        val name = st.nextToken().substring(4)
                        var expirationDate = st.nextToken().substring(6)
                        expirationDate = expirationDate.substring(0, expirationDate.length - 1) // 맨 뒤 괄호 제거

                        // 유통기한이 2로 시작하면
                        if (expirationDate.startsWith("2")) {
                            val item = Item(name, expirationDate, memo)

                            // 유통기한이 지난 경우 데이터베이스에서 아이템 삭제
                            if (isExpiredItem(item.expirationDate)) {
                                viewModel.deleteItem(item)
                                Log.e("ItemActivity", "${item.name}의 유통기한이 지남, ${item.expirationDate}")
                            }
                            // 유통기한이 지나지 않았으면 viewModel에 넣어주기
                            else {
                                viewModel.addItem(item)
                                if (!allItems.contains(item))
                                    allItems.add(item)
                            }
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    // 호출된 시점 기준으로 유통기한이 지났으면 true, 아니면 false
    private fun isExpiredItem(expirationDate: String): Boolean {
        val now = System.currentTimeMillis()
        val date = Date(now)
        val dataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val time = dataFormat.format(date)

        // 현재 년월일
        val nowYearStr = time.substring(0, 4)
        val nowMonthStr = time.substring(5, 7)
        val nowDayStr = time.substring(8, 10)
        val nowYear = nowYearStr.toInt()
        val nowMonth = nowMonthStr.toInt()
        val nowDay = nowDayStr.toInt()

        // 유통기한 년월일
        val yearStr = expirationDate.substring(0, 4)
        val monthStr = expirationDate.substring(5, 7)
        val dayStr = expirationDate.substring(8, 10)
        val year = yearStr.toInt()
        var month = monthStr.toInt()
        var day = dayStr.toInt()

        var temp = year - nowYear // 년도의 차
        if (temp < 0) // 년도의 차가 음수면 유통기한이 이미 지난 것
            return true

        month += temp * 12

        temp = month - nowMonth
        if (temp < 0) // 월 차가 음수면 유통기한이 이미 지난 것
            return true
        day += temp * 31 // 일 차가 음수면 유통기한이 이미 지난 것
        if (day < nowDay) return true

        return false
    }
}

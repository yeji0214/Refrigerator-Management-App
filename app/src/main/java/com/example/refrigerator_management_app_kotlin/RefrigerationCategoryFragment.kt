package com.example.refrigerator_management_app_kotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import java.util.StringTokenizer

/**
 * A simple [Fragment] subclass.
 * Use the [RefrigerationCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RefrigerationCategoryFragment : Fragment() {
    private lateinit var userUid: String
    private lateinit var refrigeratorId: String

    private lateinit var categoryActivity: CategoryActivity
    private lateinit var addCategoryDialog: AddCategoryDialog
    private lateinit var addRefrigerationCategoryBtn: Button

    private lateinit var viewModel: CategoryViewModel
    private lateinit var recyclerView: RecyclerView
    //private lateinit var adapter: RefrigerationCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* ---------- CategoryActivity에서 uid, id 가져오기 ---------- */
        val categoryActivity = requireActivity() as? CategoryActivity
        userUid = categoryActivity?.getUserUid() ?: ""
        refrigeratorId = categoryActivity?.getRefrigeratorId() ?: ""


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_refrigeration_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* ---------- 초기화 ---------- */
        categoryActivity = requireActivity() as CategoryActivity
        addRefrigerationCategoryBtn = view.findViewById(R.id.addRefrigerationCategoryBtn)
        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        recyclerView = view.findViewById(R.id.refrigerationCategoryRecyclerView)
//        adapter = RefrigerationCategoryAdapter(viewModel, requireContext())
//        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        /* ---------- 리스너 ---------- */
        addRefrigerationCategoryBtn.setOnClickListener { addRefrigerationCategoryBtnClick(it) }

        registerForContextMenu(recyclerView)

        val refrigerationObserver = Observer<ArrayList<String>> { strings ->
            //adapter.notifyDataSetChanged() // 어댑터에게 데이터가 변경되었다는 것을 알림
        }
        viewModel.refrigerationCategorysLiveData.observe(viewLifecycleOwner, refrigerationObserver)
    }

    override fun onStart() {
        super.onStart()
        showRefrigerationCategoryList()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //requireActivity().menuInflater.inflate(R.menu.category_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.deleteCategory -> {
                // 카테고리 삭제
                viewModel.deleteRefrigerationCategory(viewModel.longClickPosition)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun showRefrigerationCategoryList() {
        FirebaseDatabase.getInstance().getReference().child("UserAccount").child(userUid).child("냉장고").child(refrigeratorId).child("냉장").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val v = snapshot.value
                if (v == null)
                    return

                val value = v.toString()
                Log.e("RefrigerationCategoryFragment", "value = $value")
                var tokens = value.split("\\}\\},".toRegex()).toTypedArray()
                if (tokens.isNotEmpty()) tokens[0] = tokens[0].substring(1, tokens[0].length) // 맨 앞 괄호 없애기

                tokens = tokens.map { it.trim() }.toTypedArray() // 앞뒤 공백 제거

                tokens.forEach {
                    val st = StringTokenizer(it, "=")
                    viewModel.addRefrigerationCategory(st.nextToken().trim())
                    Log.e("RefrigerationCategoryFragment", "viewModel에 추가 : $it")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    // 냉장 카테고리 추가하는 버튼을 누르면 실행되는 함수
    private fun addRefrigerationCategoryBtnClick(v: View) {
        addCategoryDialog = AddCategoryDialog(requireContext(), viewModel, "냉장")
        addCategoryDialog.show()
    }
}


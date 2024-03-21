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
    private var userUid: String? = null
    private var refrigeratorId: String? = null
    private var categoryActivity: CategoryActivity? = null
    //private var addCategoryDialog: AddCategoryDialog? = null
    private var addRefrigerationCategoryBtn: Button? = null
    //private var viewModel: CategoryViewModel? = null
    private var recyclerView: RecyclerView? = null
    //private var adapter: RefrigerationCategoryAdapter? = null
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mParam1 = it.getString(ARG_PARAM1)
            mParam2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        userUid = (activity as CategoryActivity?)?.getUserUid()
        refrigeratorId = (activity as CategoryActivity?)?.getRefrigeratorId()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_refrigeration_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryActivity = activity as CategoryActivity?
        addRefrigerationCategoryBtn = view.findViewById(R.id.addRefrigerationCategoryBtn)
        //viewModel = activity?.viewModel
        recyclerView = view.findViewById(R.id.refrigerationCategoryRecyclerView)
//        adapter = RefrigerationCategoryAdapter(viewModel, requireContext())
//        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.setHasFixedSize(true)

        addRefrigerationCategoryBtn?.setOnClickListener { v: View? -> addRefrigerationCategoryBtnClick(v) }

        //registerForContextMenu(recyclerView)

        val refrigerationObserver = Observer<ArrayList<String>> { strings ->
            //adapter?.notifyDataSetChanged()
        }
        //viewModel?.refrigerationCategorysLiveData?.observe(viewLifecycleOwner, refrigerationObserver)
    }

    override fun onStart() {
        super.onStart()
        showRefrigerationCategoryList2()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        val inflater: MenuInflater = requireActivity().menuInflater
        //inflater.inflate(R.menu.category_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.deleteCategory -> {
                //viewModel?.deleteRefrigerationCategory(viewModel?.longClickPosition ?: 0)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun showRefrigerationCategoryList2() {
        FirebaseDatabase.getInstance().getReference().child("UserAccount").child(userUid!!).child("냉장고").child(refrigeratorId!!).child("냉장").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val v = snapshot.value
                if (v == null) return

                val value = v.toString()
                Log.e("RefrigerationCategoryFragment", "value = $value")
                var tokens = value.split("\\}\\},".toRegex()).toTypedArray()
                if (tokens.isNotEmpty()) tokens[0] = tokens[0].substring(1, tokens[0].length) // 맨 앞 괄호 없애기

                for (i in tokens.indices) tokens[i] = tokens[i].trim() // 앞뒤 공백 제거

                for (token in tokens) {
                    val st = StringTokenizer(token, "=")
                    //viewModel?.addRefrigerationCategory(st.nextToken().trim())
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun addRefrigerationCategoryBtnClick(v: View?) {
//        addCategoryDialog = AddCategoryDialog(requireContext(), viewModel, "냉장")
//        addCategoryDialog?.show()
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance(param1: String, param2: String): RefrigerationCategoryFragment {
            val fragment = RefrigerationCategoryFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}

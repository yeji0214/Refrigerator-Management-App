package com.example.refrigerator_management_app_kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.util.Log
import androidx.fragment.app.Fragment

class ItemInformationFragment(private val clickName: String) : Fragment() {
    private lateinit var item: Item
    private lateinit var nameEditText: EditText
    private lateinit var expirationDateEditText: EditText
    private lateinit var memoEditText: EditText
    private lateinit var viewModel: ItemViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* ---------- View Model ---------- */
        viewModel = (requireActivity() as ItemActivity).fetchViewModel()

        /* ---------- 클릭한 item 가져와서 이름, 유통기한, 메모 초기화 ---------- */
        Log.e("ItemInformationFragment", "클릭한 item name = $clickName")
        val index = viewModel.getItemIndex(clickName)
        //item = viewModel.getItem(index)

        nameEditText = requireActivity().findViewById(R.id.nameEditText)
        expirationDateEditText = requireActivity().findViewById(R.id.expirationDateEditText)
        memoEditText = requireActivity().findViewById(R.id.memoEditText)

//        nameEditText.setText(item.name)
//        expirationDateEditText.setText(item.expirationDate)
//        memoEditText.setText(item.memo)
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) = ItemInformationFragment().apply {
//            arguments = Bundle().apply {
//                putString(ARG_PARAM1, param1)
//                putString(ARG_PARAM2, param2)
//            }
//        }
//
//        private const val ARG_PARAM1 = "param1"
//        private const val ARG_PARAM2 = "param2"
//    }
}

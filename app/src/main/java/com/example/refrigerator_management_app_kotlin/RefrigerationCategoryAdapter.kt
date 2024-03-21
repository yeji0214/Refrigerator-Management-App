package com.example.refrigerator_management_app_kotlin

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RefrigerationCategoryAdapter(private val viewModel: CategoryViewModel, private val context: Context) :
    RecyclerView.Adapter<RefrigerationCategoryAdapter.ViewHolder>() {

    // ViewHolder Class - 아이템 뷰를 저장
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val category: TextView = itemView.findViewById(R.id.category)

        init {
            category.setOnClickListener {
                val textView = it as TextView
                val intent = Intent(context, ItemActivity::class.java).apply {
                    putExtra("userUid", viewModel.getUserUid())
                    putExtra("ID", viewModel.getRefrigeratorId())
                    putExtra("category", textView.text.toString()) // intent에 클릭한 카테고리 이름 넣어주기
                    putExtra("type", "냉장")
                }
                context.startActivity(intent) // 액티비티 이동
            }
            category.setOnLongClickListener {
                Log.e("Category ViewHolder", "Long Click Position = $adapterPosition")
                viewModel.longClickPosition = adapterPosition
                false
            }
        }

        fun setContents(pos: Int) {
            val text = viewModel.refrigerationCategorys[pos]
            category.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.recyclerview_category, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContents(position)
    }

    override fun getItemCount(): Int {
        return viewModel.getRefrigerationCategorySize()
    }
}

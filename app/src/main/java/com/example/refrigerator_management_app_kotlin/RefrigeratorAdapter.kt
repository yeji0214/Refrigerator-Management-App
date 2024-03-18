package com.example.refrigerator_management_app_kotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RefrigeratorAdapter(private val context: Context, private val viewModel: RefrigeratorViewModel, private val userUid: String) :
    RecyclerView.Adapter<RefrigeratorAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        private val refriPlusImage: ImageView = itemView.findViewById(R.id.refri_plusImage)
        private val refriPlustext: TextView = itemView.findViewById(R.id.refri_plustext)

        init {
            // 냉장고 클릭시 카테고리 화면으로 이동.
            itemView.setOnClickListener {
//                val intent = Intent(context, CategoryActivity::class.java)
//                intent.putExtra("userUid", userUid)
//                intent.putExtra("refrigeratorID", refriPlustext.text.toString())
//                context.startActivity(intent)
            }

            itemView.setOnCreateContextMenuListener(this) // 냉장고를 롱클릭 했을 때 컨텍스트 메뉴가 보이도록 설정한다.

            // 냉장고를 롱클릭 했을 때 몇 번째 위치에 있는지 알 수 있도록
            // RefrigeratorAdapter의 메소드인 getAdapterPosition을 통해
            // 위치를 알아낸 후 setItemPos를 함.
            itemView.setOnLongClickListener { view ->
                viewModel.setPosition(adapterPosition)
                false
            }
        }

        override fun onCreateContextMenu(contextMenu: ContextMenu, view: View, contextMenuInfo: ContextMenu.ContextMenuInfo?) {
            (view.context as Activity).menuInflater.inflate(R.menu.refrigerator_menu, contextMenu)
        }

        fun setContents(position: Int) {
            refriPlustext.text = viewModel.refrigerators[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.recyclerview_refrigerator, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContents(position)
    }

    override fun getItemCount(): Int {
        return viewModel.refrigeratorsSize
    }
}

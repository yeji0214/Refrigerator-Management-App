package com.example.refrigerator_management_app_kotlin

import android.graphics.Rect
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

class RefrigeratorRecyclerviewDeco(private val divWidth: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = divWidth
        outRect.right = divWidth
    }
}
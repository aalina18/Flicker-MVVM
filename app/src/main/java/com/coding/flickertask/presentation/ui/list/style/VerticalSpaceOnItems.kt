package com.coding.flickertask.presentation.ui.list.style

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class VerticalSpaceOnItems(private val verticalSpaceHeight: Int) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State) {
        outRect.bottom = verticalSpaceHeight
    }
}
package com.coding.flickertask.presentation.ui.list.callback

import com.coding.flickertask.model.PhotoResponse

interface OnItemClickListener {
    fun onItemClick(item: PhotoResponse)
}
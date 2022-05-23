package com.coding.flickertask.presentation.ui.list.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.coding.flickertask.model.PhotoResponse
import com.coding.flickertask.presentation.ui.list.callback.OnItemClickListener
import com.coding.flickertask.presentation.ui.list.PhotoViewHolder

class PhotosItemAdapter(
    private val listener: OnItemClickListener
) : PagingDataAdapter<PhotoResponse, PhotoViewHolder>(PHOTO_COMPARATOR) {

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<PhotoResponse>() {
            override fun areItemsTheSame(oldItem: PhotoResponse, newItem: PhotoResponse): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PhotoResponse, newItem: PhotoResponse): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let { photo ->
            holder.bind(photo)
        }
    }
}

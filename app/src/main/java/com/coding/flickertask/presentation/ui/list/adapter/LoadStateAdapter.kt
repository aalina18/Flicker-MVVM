package com.coding.flickertask.presentation.ui.list.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.coding.flickertask.presentation.ui.list.FooterView

class PhotosLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<FooterView>() {
    override fun onBindViewHolder(holder: FooterView, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FooterView {
        return FooterView.create(parent, retry)
    }
}
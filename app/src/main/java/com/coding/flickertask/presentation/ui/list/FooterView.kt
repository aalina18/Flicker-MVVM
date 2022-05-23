package com.coding.flickertask.presentation.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.coding.flickertask.R
import com.coding.flickertask.databinding.FooterViewBinding

class FooterView(
    private val binding: FooterViewBinding,
    retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.loadingItemProgressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState !is LoadState.Loading
            binding.errorMsg.isVisible = loadState !is LoadState.Loading
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): FooterView {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.footer_view, parent, false)
                val binding = FooterViewBinding.bind(view)
                return FooterView(binding, retry)
            }
        }
    }
/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coding.flickertask.presentation.ui.list

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.coding.flickertask.R
import com.facebook.drawee.view.SimpleDraweeView
import com.coding.flickertask.model.PhotoResponse
import com.coding.flickertask.presentation.ui.list.callback.OnItemClickListener
import com.coding.flickertask.util.buildPhotoUri

/**
 * View Holder for a [Photo] RecyclerView list item.
 */
class PhotoViewHolder(view: View, private val listener: OnItemClickListener) :
    RecyclerView.ViewHolder(view) {
    private val imageName: TextView = view.findViewById(R.id.imageTittle)
    private val imageView: SimpleDraweeView = view.findViewById(R.id.imageView)
    private var photo: PhotoResponse? = null

    init {
        view.setOnClickListener {
            photo?.let { photo ->
                val url = buildPhotoUri(photo.farm,photo.server,photo.id,photo.secret)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(photo: PhotoResponse?) {
        if (photo == null) {
            val resources = itemView.resources
            imageName.text = resources.getString(R.string.loading_text)
        } else {
            showData(photo)
        }
    }

    private fun showData(photo: PhotoResponse) {
        this.photo = photo
        imageName.text = photo.title
        val url = buildPhotoUri(photo.farm,photo.server,photo.id,photo.secret)
        imageView.setImageURI(url)

    }

    companion object {
        fun create(parent: ViewGroup, listener: OnItemClickListener): PhotoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_item, parent, false)
            return PhotoViewHolder(view, listener)
        }
    }
}

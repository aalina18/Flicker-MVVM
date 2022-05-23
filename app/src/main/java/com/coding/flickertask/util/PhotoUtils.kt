package com.coding.flickertask.util

fun buildPhotoUri(farm: Int, server: String, id: String, secret: String): String {
    return  "https://farm${farm}.static.flickr.com/${server}/${id}_${secret}.jpg"
}
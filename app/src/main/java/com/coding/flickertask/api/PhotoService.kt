package com.coding.flickertask.api

import com.coding.flickertask.model.PhotosSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GetPhotoService {

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=3&safe_search=1")
    suspend fun getPics(@Query("text") searchText: String, @Query("page") page: Int,@Query("api_key") apiKey: String): PhotosSearchResponse
}

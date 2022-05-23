package com.coding.flickertask.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.coding.flickertask.api.GetPhotoService
import com.coding.flickertask.model.PhotoResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import com.coding.flickertask.BuildConfig
@ExperimentalCoroutinesApi
@FlowPreview

class PhotoRepository(private val service: GetPhotoService)  {

    companion object {
        private const val PAGE_SIZE = 5
    }

    /**
     * Fetch photos, expose them as a stream of data that will emit
     * every time we get more data from the network.
     */
    fun getPhotos(searchText: String): Flow<PagingData<PhotoResponse>> {
        Log.d("PixaBayRepository", "New page")
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { PhotoPagingSource(service, searchText, BuildConfig.API_KEY) }
        ).flow
   }
}

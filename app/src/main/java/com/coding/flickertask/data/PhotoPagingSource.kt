package com.coding.flickertask.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.coding.flickertask.api.GetPhotoService
import com.coding.flickertask.model.PhotoResponse
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE = 1

class PhotoPagingSource(
    private val service: GetPhotoService,
    private val searchText: String,
    private val apiKey: String,
) : PagingSource<Int, PhotoResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoResponse> {
        val position = params.key ?: STARTING_PAGE
        return try {
            val response = service.getPics(searchText = searchText, page = position, apiKey = apiKey)
            val photos = response.photos
            LoadResult.Page(
                data = photos.photo,
                prevKey = if (position == STARTING_PAGE) null else position,
                nextKey = if (photos.photo.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }catch (exception: Exception){
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PhotoResponse>): Int? {
        TODO("Not yet implemented")
    }
}
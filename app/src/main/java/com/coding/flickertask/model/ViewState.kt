package com.coding.flickertask.model

import android.view.View
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.coding.flickertask.presentation.base.*

data class PhotoFragmentViewState(
    val page: PagingData<PhotoResponse>? = null,
    val adapter: List<PhotoResponse> = emptyList(),
    val errorMessageResource: Int? = null,
    val errorMessage: String? = null,
    val loadingStateVisibility: Int? = null,
    val errorStateVisibility: Int? = View.GONE
) : BaseViewState

sealed class ViewEffect : BaseViewEffect {
    data class TransitionToScreen(val photo: PhotoResponse) : ViewEffect()
}

sealed class PhotoFragmentEvent : BaseEvent {
    object pullToRefreshEvent : PhotoFragmentEvent()
    object loadLocallySaved : PhotoFragmentEvent()
    data class LoadState(val state: CombinedLoadStates) : PhotoFragmentEvent()
    data class ListItemClicked(val item: PhotoResponse) : PhotoFragmentEvent()
    object ScreenLoad : PhotoFragmentEvent()
}

sealed class Result : BaseResult {
    data class Error(val errorMessage: String?) : Result()
    data class Content(val content: PagingData<PhotoResponse>) : Result()
}
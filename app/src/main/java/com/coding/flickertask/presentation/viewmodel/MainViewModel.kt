package com.coding.flickertask.presentation.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.coding.flickertask.data.PhotoRepository
import com.coding.flickertask.di.repository.LocalStorageRepository
import com.coding.flickertask.model.*
import com.coding.flickertask.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ExperimentalCoroutinesApi constructor(
    private val pixBayRepository: PhotoRepository,
    private val localStorageRepository: LocalStorageRepository
) : BaseViewModel<PhotoFragmentViewState, ViewEffect, PhotoFragmentEvent, Result>() {

    private var currentViewState = PhotoFragmentViewState()
        set(value) {
            field = value
            viewStateLD.postValue(value)
        }

    private val viewAction = MutableLiveData<ViewEffect>()

    private val userHistory = MutableLiveData<MutableList<String>>()

    val obtainViewEffects: LiveData<ViewEffect> = viewAction

    val obtainState: LiveData<PhotoFragmentViewState> = viewState

    val userHistoryState: LiveData<MutableList<String>> = userHistory

    private fun fetchData(searchText: String) {
        resultToViewState(ResponseGeneric.Loading())
        getPhotosFlow(searchText)
        viewModelScope.launch {
            localStorageRepository.savePhoneBook(FetchImageRequest(searchQuery = searchText))
        }
    }

    private fun getPhotosFlow(searchText: String) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch(Dispatchers.IO) {
            pixBayRepository.getPhotos(searchText)
                .cachedIn(viewModelScope)
                .onEach {}
                .collect { results ->
                    resultToViewState(ResponseGeneric.ResponseData(Result.Content(results)))
                }
        }
    }

    private fun getUserLocalHistory() {
        localStorageRepository.getPhoneBook()?.split(",")?.let {
            userHistory.postValue(localStorageRepository.getPhoneBook()?.split(",") as MutableList<String>?)
        }
    }

    override fun eventToResult(photoFragmentEvent: PhotoFragmentEvent) {
        when (photoFragmentEvent) {
            is PhotoFragmentEvent.ListItemClicked -> viewAction.postValue(ViewEffect.TransitionToScreen(photoFragmentEvent.item))
            is PhotoFragmentEvent.LoadState -> onLoadState(photoFragmentEvent.state)
            is PhotoFragmentEvent.loadLocallySaved -> getUserLocalHistory()
            else -> {}
        }
    }

    private fun onLoadState(state: CombinedLoadStates) {
        // TODO: Add mapper from throwable to human readable message
        when (state.source.refresh) {
            is LoadState.Error -> {
                val errorState = state.source.append as? LoadState.Error
                    ?: state.source.prepend as? LoadState.Error
                    ?: state.append as? LoadState.Error
                    ?: state.prepend as? LoadState.Error
                errorState?.let {
                    resultToViewState(ResponseGeneric.Error(Result.Error(errorMessage = errorState.error.localizedMessage)))
                }
            }
            is LoadState.Loading -> resultToViewState(ResponseGeneric.Loading())
            else -> {}
        }

    }

    override suspend fun suspendEventToResult(photoFragmentEvent: PhotoFragmentEvent, searchText: String) {
        when (photoFragmentEvent) {
            is PhotoFragmentEvent.ScreenLoad, PhotoFragmentEvent.pullToRefreshEvent -> fetchData(searchText)
            else -> {}
        }
    }

    override fun resultToViewState(result: ResponseGeneric<Result>) {
        currentViewState = when (result) {
            //Loading state
            is ResponseGeneric.Loading -> {
                currentViewState.copy(
                    loadingStateVisibility = View.VISIBLE,
                    errorStateVisibility = View.GONE
                )
            }
            //Content state
            is ResponseGeneric.ResponseData -> {
                when (result.packet) {
                    is Result.Content ->
                        currentViewState.copy(
                            page = result.packet.content,
                            loadingStateVisibility = View.GONE,
                            errorStateVisibility = View.GONE
                        )
                    else -> currentViewState.copy()
                }
            }
            //Error state
            is ResponseGeneric.Error -> {
                when (result.packet) {
                    is Result.Error ->
                        currentViewState.copy(
                            errorStateVisibility = View.VISIBLE,
                            errorMessage = result.packet.errorMessage,
                            loadingStateVisibility = View.GONE
                        )
                    else -> currentViewState.copy()
                }
            }
        }
    }

    override fun resultToViewEffect(result: ResponseGeneric<Result>) {
        TODO("Not yet implemented")
    }
}
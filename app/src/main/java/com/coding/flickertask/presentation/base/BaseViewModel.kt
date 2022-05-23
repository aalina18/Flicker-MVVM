package com.coding.flickertask.presentation.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coding.flickertask.model.ResponseGeneric
import kotlinx.coroutines.Job

abstract class BaseViewModel<
        ViewState : BaseViewState,
        ViewAction : BaseViewEffect,
        Event : BaseEvent,
        Result : BaseResult> : ViewModel() {
    internal val viewStateLD = MutableLiveData<ViewState>()
    private val viewEffectLD = MutableLiveData<ViewAction>()

    val viewState: LiveData<ViewState> get() = viewStateLD
    val viewEffects: LiveData<ViewAction> get() = viewEffectLD

   var loadJob: Job? = null

    fun onEvent(event: Event) {
        Log.d("Zivi", "----- event ${event.javaClass.simpleName}")
        eventToResult(event)
    }

    fun getLocallySavedData(event: Event) {
        eventToResult(event)
    }
    suspend fun onSuspendedEvent(event: Event, searchText: String) {
        Log.e("Zivi 1", "----- suspend event ${event.javaClass.simpleName}")
        suspendEventToResult(event, searchText)
    }

    abstract fun eventToResult(event: Event)

    abstract suspend fun suspendEventToResult(event: Event, searchText: String)

    abstract fun resultToViewState(result: ResponseGeneric<Result>)

    abstract fun resultToViewEffect(result: ResponseGeneric<Result>)
}

interface BaseViewState

interface BaseViewEffect

interface BaseEvent

interface BaseResult

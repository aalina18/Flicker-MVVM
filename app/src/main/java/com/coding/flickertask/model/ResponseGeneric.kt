package com.coding.flickertask.model

sealed class ResponseGeneric<T> {
    class Loading<T> : ResponseGeneric<T>() {
        override fun equals(other: Any?) = (other is Loading<*>)
        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }
    data class ResponseData<T>(val packet: T) : ResponseGeneric<T>()
    data class Error<T>(val packet: T) : ResponseGeneric<T>()
}
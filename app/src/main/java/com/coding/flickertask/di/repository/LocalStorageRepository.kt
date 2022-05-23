package com.coding.flickertask.di.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.coding.flickertask.model.FetchImageRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@ExperimentalCoroutinesApi
@FlowPreview

const val DataStore_NAME = "USER_HISTORY"

class LocalStorageRepository(private val context: Context) : ILocalStorageRepository {

    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences(DataStore_NAME, Context.MODE_PRIVATE)

    companion object {
        val SEARCH_QUERY = stringPreferencesKey("QUERY_STRING")
    }

    override fun savePhoneBook(historyData: FetchImageRequest) {
        var editor = sharedPreference.edit()
        editor.putString(SEARCH_QUERY.toString(), historyData.searchQuery + "," + getPhoneBook())
        editor.apply()
    }

    override fun getPhoneBook(): String? = sharedPreference.getString(SEARCH_QUERY.toString(), "")
}
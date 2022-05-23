package com.coding.flickertask.di.repository

import com.coding.flickertask.model.FetchImageRequest

interface ILocalStorageRepository {

      fun savePhoneBook(phonebook: FetchImageRequest)

      fun getPhoneBook():String?
}
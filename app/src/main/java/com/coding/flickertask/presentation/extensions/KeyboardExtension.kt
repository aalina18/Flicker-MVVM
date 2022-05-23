package com.coding.flickertask.presentation.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
}

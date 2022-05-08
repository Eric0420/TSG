package com.kit301.tsgapp.ui.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MoreViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Expanded navigation view here."
    }
    val text: LiveData<String> = _text

}
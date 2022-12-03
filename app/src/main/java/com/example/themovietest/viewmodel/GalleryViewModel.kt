package com.example.themovietest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Gallery Fragment"
    }
    val text: LiveData<String> = _text
}
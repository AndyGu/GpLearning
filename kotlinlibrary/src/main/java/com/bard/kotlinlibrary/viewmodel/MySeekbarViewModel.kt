package com.bard.kotlinlibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MySeekbarViewModel: ViewModel() {
    private var seekProcess = MutableLiveData<Int>()

    fun getProgress(): MutableLiveData<Int>{
        return seekProcess;
    }
}
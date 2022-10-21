package com.bard.kotlinlibrary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

//
class MyNumberViewModel : ViewModel() {
    var number: Int = 0

    class My2ViewModel(application: Application) : AndroidViewModel(application) {
        var data : Int = 0
    }
}


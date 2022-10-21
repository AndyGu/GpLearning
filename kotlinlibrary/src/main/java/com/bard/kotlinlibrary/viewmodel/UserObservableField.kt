package com.bard.kotlinlibrary.viewmodel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import com.bard.kotlinlibrary.BR
import com.bard.kotlinlibrary.bean.User

/**
 * 这与之前的ViewModel不是一回事
 */
class UserObservableField : BaseObservable(){
    val user = User("Sony")
    var observableField : ObservableField<User> = ObservableField()

    init {
        observableField.set(user)
    }


    fun getName(): String?{
        Log.e("TAG", "getUserName observableField.get()=${observableField.get()?.name }")
        return observableField.get()?.name
    }

    fun setName(name: String){
        Log.e("TAG", "setUserName name=${name} --- ${observableField.get()}")
        observableField.get()?.name = name

//        observableField.notifyPropertyChanged(BR.name)
    }
}
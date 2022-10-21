package com.bard.kotlinlibrary.viewmodel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.bard.kotlinlibrary.BR
import com.bard.kotlinlibrary.bean.User

/**
 * 这与之前的ViewModel不是一回事
 */
class UserBaseObservable : BaseObservable(){
    var user : User = User("Jacky")

//    class UserBean : BaseObservable() {
//        @get:Bindable
//        var firstName: String? = null
//            set(firstName) {
//                field = firstName
//                // 每当值set()后，通过notifyPropertyChanged()方法去指定更新
//                // 可更新某个值，可以更新整个数据，取决于你BR后面的属性
//                // BR._all 可更新所有的BR中字段相关联的UI
//                //这里只更新firstName属性
//                notifyPropertyChanged(BR.firstName)
//            }
//        @get:Bindable
//        var lastName: String? = null
//            set(lastName) {
//                field = lastName
//                notifyPropertyChanged(BR.lastName)
//            }
//    }


    @Bindable
    fun getName(): String{
        return user.name
    }

    fun setName(userName: String){
        Log.e("setUserName","userName=$userName")
        if(userName != user.name){
            user.name = userName
            Log.e("setUserName","user.name=$user.name")
            notifyPropertyChanged(BR.name)
        }
    }
}
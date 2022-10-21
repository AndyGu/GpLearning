package com.bard.kotlinlibrary.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import com.bard.kotlinlibrary.R
import com.bard.kotlinlibrary.databinding.ActivityBindingObservablefieldBinding
import com.bard.kotlinlibrary.viewmodel.UserObservableField
import kotlinx.android.synthetic.main.activity_binding_observablefield.*


class BindingObservableFieldActivity : AppCompatActivity() {

    lateinit var userObservableField : UserObservableField

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mBinding: ActivityBindingObservablefieldBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_binding_observablefield)
        userObservableField = UserObservableField()
        mBinding.userObservableField = userObservableField
//        mBinding.userObservableField?.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
//            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
//                Log.e("onCreate","onPropertyChanged")
//            }
//        })
//
//        mBinding.userObservableField?.observableField?.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback(){
//            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
//                Log.e("onCreate","onPropertyChanged 1111")
//            }
//        })

        button.setOnClickListener {
//            val field : UserObservableField? = mBinding.userObservableField
//            field?.setName("ppooll")
            //TODO 不起作用
            userObservableField.setName("1122334")
            Toast.makeText(this, "不起作用", Toast.LENGTH_SHORT).show()

//            mBinding.etName.setText("aabbccdd")
        }
    }
}
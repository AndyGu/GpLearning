package com.bard.kotlinlibrary.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bard.kotlinlibrary.R
import kotlinx.android.synthetic.main.activity_test.*

class ChronometerActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        lifecycle.addObserver(my_chronometer)

        btn_to_service.setOnClickListener {
//            startActivity(Intent(ChronometerActivity@this, ServiceActivity::class.java))
            startActivity(Intent(this, ServiceActivity::class.java))
        }

        btn_to_mvvm.setOnClickListener {
            startActivity(Intent(this, ViewModelActivity::class.java))
        }

        btn_to_livedata.setOnClickListener{
            startActivity(Intent(this, LiveDataActivity::class.java))
        }

        btn_to_databinding.setOnClickListener {
            startActivity(Intent(this, DataBindingActivity::class.java))
        }

        btn_to_bindingadapter.setOnClickListener{
            startActivity(Intent(this, BindingAdapterActivity::class.java))
        }

        btn_to_binding_baseobservable.setOnClickListener {
            startActivity(Intent(this, BindingBaseObservableActivity::class.java))
        }

        btn_to_binding_observablefield.setOnClickListener {
            startActivity(Intent(this, BindingObservableFieldActivity::class.java))
        }

        /**
         * setOnClickListener的简化过程
          */
//        my_btn.setOnClickListener(object : View.OnClickListener{
//            override fun onClick(v: View?) {
//            }
//        })
//
//        my_btn.setOnClickListener(View.OnClickListener {
//        })
//
//        my_btn.setOnClickListener({
//        })
//
//        my_btn.setOnClickListener {
//        }
    }
}
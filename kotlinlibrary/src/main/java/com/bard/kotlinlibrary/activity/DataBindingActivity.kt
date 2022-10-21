package com.bard.kotlinlibrary.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bard.kotlinlibrary.R
import com.bard.kotlinlibrary.bean.Idol
import com.bard.kotlinlibrary.databinding.ActivityDatabindingBinding
import com.bard.kotlinlibrary.interfaces.EventHandlerListener

class DataBindingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mBinding: ActivityDatabindingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_databinding)
        mBinding.idol = Idol("Andy", "男", 12, "他是一个多愁善感的人")
        mBinding.eventHandler = EventHandlerListener(this)
    }
}
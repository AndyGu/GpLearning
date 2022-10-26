package com.bard.kotlinlibrary.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bard.kotlinlibrary.R
import com.bard.kotlinlibrary.databinding.ActivityBindingAdapterBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BindingAdapterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mBinding: ActivityBindingAdapterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_binding_adapter)
        mBinding.onlineImage = "https://profile.csdnimg.cn/C/9/2/1_gupengnn"
        mBinding.localImage = R.drawable.wait

        /**
         * startActivityForResult的替代
         *
         * launch()方法，输入Intent
         * ActivityResultCallback:获取返回的数据，
         * ActivityResultContracts.StartActivityForResult 是官方提供用来处理回调数据的ActivityResultContract类
         * 跳转到 DataBindingActivity 后，调用setResult()方法传递数据，这部分和以前一样
         */
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(), object : ActivityResultCallback<ActivityResult>{
            override fun onActivityResult(result: ActivityResult?) {
                if(result != null){
                    val data = result.data as Intent
                    val resultCode : Int = result.resultCode
                }
            }
        }).launch(Intent(this, DataBindingActivity::class.java))
    }
}
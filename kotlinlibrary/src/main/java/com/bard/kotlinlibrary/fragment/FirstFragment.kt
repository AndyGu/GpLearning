package com.bard.kotlinlibrary.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bard.kotlinlibrary.R
import com.bard.kotlinlibrary.viewmodel.MySeekbarViewModel
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_second.*
import java.math.BigInteger

class FirstFragment : Fragment() {

    private lateinit var mySeekbarViewModel: MySeekbarViewModel

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        Log.e("FirstFragment", "onAttach activity")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("FirstFragment", "onAttach context")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("FirstFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("FirstFragment", "onCreateView")
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("FirstFragment", "onViewCreated")

        /**
         * 这里 ViewModelProvider 的参数 requireActivity() 非常关键
         * 表示 ViewModel 的生命周期是跟着 Activity的，如果传 this，就变成了 Fragment的
         * 这样在 FirstFragment 和 SecondFragment 中就是两个不同的ViewModel了
         *
         * 但是如果绑定的是Activity，当Fragment销毁，那么
         */
        mySeekbarViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(MySeekbarViewModel::class.java)
        mySeekbarViewModel.getProgress().observe(requireActivity(), object : Observer<Int>{
            override fun onChanged(t: Int?) {
                Log.e("FirstFragment", "onChanged t=$t")
                seekBar_first.progress = t ?: 0
            }
        })

        seekBar_first.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.e("FirstFragment", "onProgressChanged progress=$progress")
                mySeekbarViewModel.getProgress().value = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("FirstFragment", "onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.e("FirstFragment", "onStart")
    }

    companion object {
        private val otherField = 0

        @JvmField
        val BIG_INTEGER = BigInteger.ONE

        //非静态方法，kotlin会创建内部伴生类，利用伴生对象，达到单例的效果
        fun newnew(param1: String): FirstFragment{
            var fragment =  FirstFragment()
            var bundle = Bundle()
            bundle.putString("abc", param1)
            fragment.arguments = bundle
            return fragment
        }

        //静态方法
        @JvmStatic
        fun newInstance(param1: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                    putString("abc", param1)
                }
            }
    }
}


class TestStatic {
    private val otherField = 0

    companion object {
        val BIG_INTEGER = BigInteger.ONE

        fun method() {
            println("call method")
        }
    }
}
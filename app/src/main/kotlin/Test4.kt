package com.bard.gplearning.lifecycle

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

fun main(){
    println("1111========${Date().getNowDateTime()}")
    //调用协程方法
    println("2222========${Date().getNowDateTime()}")
}

/**
 * 获取当前的年月日,时分秒
 */
fun Date.getNowDateTime(): String {
    val sdf = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
    return sdf.format(this)
}

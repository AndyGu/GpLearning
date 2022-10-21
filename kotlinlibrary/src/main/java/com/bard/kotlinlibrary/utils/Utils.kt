package com.bard.kotlinlibrary.utils

object Utils {
    fun getStars(star : Int): String{
        return when(star){
            1 -> "一颗星"
            2 -> "二颗星"
            3 -> "三颗星"
            4 -> "四颗星"
            5 -> "四颗星"
            else -> ""
        }
    }

    fun getSex(sex : String): String{
        return when(sex){
            "男" -> "性别：$sex"
            "女" -> "性别：$sex"
            else -> "error"
        }
    }
}
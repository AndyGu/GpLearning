package com.bard.gplearning.widgets.hencoder

import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.bard.gplearning.extensions.dp

private val provinces = listOf("北京市", "天津市", "黑龙江省", "辽宁省", "西藏省", "上海市", "云南省", "四川省", "广西省", "重庆市", "广东省", "福建省", "澳门特别行政区", "河北省", "山西省", "江苏省", "台湾省")

class ProvinceView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        textSize = 80.dp
        textAlign = Paint.Align.CENTER
    }

    var province = "北京市"
        set(value){
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)

        canvas.drawText(province, width/2f, height/2f, paint)
    }
}

class ProvinceEvaluator: TypeEvaluator<String> {
    override fun evaluate(fraction: Float, startValue: String, endValue: String): String{
        //用序号算
        val startIndex = provinces.indexOf(startValue)
        val endIndex = provinces.indexOf(endValue)
        val currentIndex = startIndex + (endIndex - startIndex) * fraction
        return provinces[currentIndex.toInt()]
    }
}
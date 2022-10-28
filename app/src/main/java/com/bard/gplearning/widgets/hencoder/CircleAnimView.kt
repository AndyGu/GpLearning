package com.bard.gplearning.widgets.hencoder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.bard.gplearning.extensions.dp

class CircleAnimView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    //java写法
    //public void setRadius(float value){
    //		this.radius = value;
    //		invalidate()
    //}

    //kotlin写法
    var radius = 50.dp
        set(value){
            field = value
            invalidate()
        }

    init{
        paint.color = Color.parseColor("#00796B")
    }

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)

        canvas.drawCircle(width/2f, height/2f, radius, paint)
    }
}
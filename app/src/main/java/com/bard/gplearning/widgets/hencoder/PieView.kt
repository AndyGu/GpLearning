package com.bard.gplearning.widgets.hencoder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.bard.gplearning.extensions.dp
import kotlin.math.cos
import kotlin.math.sin


private const val SELECTED_INDEX = 1
private val RADIUS = 150f.dp
private val ANGLES = floatArrayOf(60f, 90f, 150f, 60f)
private val COLORS = listOf(Color.parseColor("#C21858"), Color.parseColor("#00ACC1"), Color.parseColor("#558B2F"), Color.parseColor("#5D4037"))
private val OFFSET_LENGTH = 20f.dp



class PieView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int){
    }

    override fun onDraw(canvas: Canvas){
        //画弧
        var startAngle = 0f
        for((index, angle) in ANGLES.withIndex()){

            paint.color = COLORS[index]
            if(index == SELECTED_INDEX){
                canvas.save()
                canvas.translate(OFFSET_LENGTH * cos(Math.toRadians(startAngle + angle/2f.toDouble())).toFloat(),
                    OFFSET_LENGTH * sin(Math.toRadians(startAngle + angle/2f.toDouble())).toFloat())
            }

            canvas.drawArc(width/2f - RADIUS, height/2f - RADIUS, width/2f + RADIUS, height/2f + RADIUS,
                startAngle, angle, true, paint)
            startAngle += angle

            if(index == SELECTED_INDEX){
                canvas.restore()
            }
        }
    }
}
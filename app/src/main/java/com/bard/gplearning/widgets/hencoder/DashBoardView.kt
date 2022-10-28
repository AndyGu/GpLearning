package com.bard.gplearning.widgets.hencoder

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.bard.gplearning.extensions.dp
import kotlin.math.cos
import kotlin.math.sin

private const val OPEN_ANGLE = 120f //底部开口角度
private const val MARK = 10
private val DASH_WIDTH = 2f.dp
private val DASH_LENGTH = 10f.dp
private val RADIUS = 150f.dp
private val LENGTH = 120f.dp

class DashBoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val dash = Path() //刻度的path
    private lateinit var pathEffect: PathEffect

    init{
        paint.strokeWidth = 3f.dp
        paint.style = Paint.Style.STROKE
        //
        dash.addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CCW)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int){
        //做path是为了pathMeasure，才能知道画的弧的长度是多少，才能知道每两个刻度之间的间隔
        path.reset()
        path.addArc(width/2f - RADIUS, height/2f - RADIUS, width/2f + RADIUS, height/2f + RADIUS,
            90 + OPEN_ANGLE/2f, 360 - OPEN_ANGLE)
        val pathMeasure = PathMeasure(path, false)

        pathEffect = PathDashPathEffect(dash,
            (pathMeasure.length - DASH_WIDTH) / 20f,
            0f,
            PathDashPathEffect.Style.ROTATE)
    }


    override fun onDraw(canvas: Canvas){
        //画弧
        //canvas.drawArc(width/2f - RADIUS, height/2f - RADIUS, width/2f + RADIUS, height/2f + RADIUS,
        //    90 + OPEN_ANGLE/2f, 360 - OPEN_ANGLE, false, paint)
        canvas.drawPath(path, paint)

        //画刻度
        //paint.pathEffect = PathDashPathEffect(dash, 50f, 0f, PathDashPathEffect.Style.ROTATE) //这样性能不好，后续改进
        paint.pathEffect = pathEffect

        //canvas.drawArc(width/2f - RADIUS, height/2f - RADIUS, width/2f + RADIUS, height/2f + RADIUS,
        //  90 + OPEN_ANGLE/2f, 360 - OPEN_ANGLE, false, paint)
        canvas.drawPath(path, paint)

        paint.pathEffect = null

        //画指针
        canvas.drawLine(width/2f, height/2f,
            width/2f + LENGTH * cos(markToRadians(MARK)).toFloat(),
            height/2f + LENGTH * sin(markToRadians(MARK)).toFloat(),
            paint)
    }


    private fun markToRadians(mark: Int): Double{
        return Math.toRadians((90 + OPEN_ANGLE/2f + (360 - OPEN_ANGLE) / 20f * mark).toDouble())
    }

}
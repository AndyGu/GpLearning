package com.bard.gplearning.widgets.hencoder

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.bard.gplearning.R
import com.bard.gplearning.extensions.dp


private val CIRCLE_COLOR = Color.parseColor("#90A4AE")
private val HIGHLIGHT_COLOR = Color.parseColor("#FF4081")
private val RING_WIDTH = 20.dp
private val RADIUS = 150.dp

/**
 * 运动记录界面
 */
class SportView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        // dp是对的
        // dp只受《像素密度》影响
        // sp受《像素密度》以及《用户对手机的全局设置》两个因素的影响
        textSize = 100.dp

        typeface = ResourcesCompat.getFont(context, R.font.sign_font)
        //isFakeBoldText 假粗，是系统给描粗的
        textAlign = Paint.Align.CENTER
    }

    private val bounds = Rect()
    private val fontMetrics = Paint.FontMetrics()

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)

        //绘制环
        paint.style = Paint.Style.STROKE
        paint.color = CIRCLE_COLOR
        paint.strokeWidth - RING_WIDTH
        canvas.drawCircle(width/2f, height/2f, RADIUS, paint)

        //绘制进度条
        paint.color = HIGHLIGHT_COLOR
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(width/2f - RADIUS,
            height/2f - RADIUS,
            width/2f + RADIUS,
            height/2f + RADIUS,
            -90f, 225f, false, paint)

        //绘制文字
        //需要先设置paint的属性
        paint.textSize = 100.dp
        paint.style = Paint.Style.FILL
        //再设置文字大小
        //水平齐了，纵向不齐，因为竖直方向是根据baseline基准
        //修正：找出偏移，修正偏移

        /**
         * 这个顶和底 是与文本无关的，是与字体有关的
            ----------top
            ----------ascent



            ----------baseline
            ----------descent
            ----------bottom
         */


        // getTextBounds 可以获得文字的相对于起始点的《左上右下边界》
        //这种修正方式适合静态文案，文案是动态就不适合了，文案从aaa -> bbb —> ppp 高度会变
        //paint.getTextBounds("abab", 0, "abab".length, bounds)
        //canvas.drawText("abab", width/2f, height/2f - (bounds.top + bounds.bottom)/2f, paint)

        //适合动态文字，与具体绘制文字无关，与字体有关
        canvas.drawText("abab", width/2f, height/2f - (fontMetrics.ascent + fontMetrics.descent)/2f, paint)




        //绘制文字2，贴边
        paint.textSize = 150.dp
        paint.textAlign = Paint.Align.LEFT
        //canvas.drawText("abab", 0f, 0f, paint)

        //合理，但会顶部留空，看具体需求【建议阅读型用这个】
        //paint.getFontMetrics(fontMetrics)
        //canvas.drawText("abab", 0f, 0f - fontMetrics.top, paint)
        //距顶部距离会小，有可能会显示不全【动态文字不适用，静态可以尝试】
        //canvas.drawText("abab", 0f, 0f - fontMetrics.ascent, paint)

        //精确贴上，但是成段看会不舒服，看需求了【观赏型文字，非成段的，可以尝试】
        paint.getTextBounds("abab", 0, "abab".length, bounds)
        canvas.drawText("abab", 0f, 0f - bounds.top.toFloat(), paint)


        //绘制文字3
        //字体会有自然的左右padding
        paint.textSize = 15.dp
        paint.getTextBounds("abab", 0, "abab".length, bounds)
        //还是会有一点点缝隙，是系统造成的
        canvas.drawText("abab", 0f-bounds.left.toFloat(), 0f-bounds.top.toFloat(), paint)
    }
}
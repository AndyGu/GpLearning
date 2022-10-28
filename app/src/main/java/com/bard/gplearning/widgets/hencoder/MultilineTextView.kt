package com.bard.gplearning.widgets.hencoder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.bard.gplearning.extensions.dp
import com.bard.gplearning.widgets.hencoder.AvatarView.Companion.getAvatar


private val IMAGE_SIZE = 150.dp
private val IMAGE_PADDING = 50.dp

class MultilineTextView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    //lorem ipsum 随意创造一些无意义的不会侵权的文段，例如www.lisum.com
    val text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec porttitor, tellus at consectetur consequat, felis eros posuere lectus, et vestibulum ante ante gravida leo. Nullam tincidunt tristique enim, id posuere odio maximus sit amet. Fusce sollicitudin gravida velit, id mattis lectus elementum finibus. Morbi scelerisque feugiat nisi, nec semper ante pulvinar a. Phasellus dignissim auctor nisl id vestibulum. Aenean et pulvinar lectus. Donec nec risus ut nunc tincidunt porta. Proin cursus dapibus tortor, quis bibendum nisl condimentum sed."


    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply{
        textSize = 16.dp
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        textSize = 16.dp
    }

    private val bitmap = getAvatar(resources, IMAGE_SIZE.toInt())

    private val fontMetrics = Paint.FontMetrics()


    override fun onDraw(canvas: Canvas){
        //使用StaticLayout容器绘制多行文字
        //val staticLayout = StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1, 0, false)
        //staticLayout.draw(canvas)

        canvas.drawBitmap(bitmap, width - IMAGE_SIZE, IMAGE_PADDING, paint)

        paint.getFontMetrics(fontMetrics)
        val measuredWidth = floatArrayOf(0f) //没用到

        //返回一个int，表示要换行了，这个是不管单词是否完整展示的，只管字母是否能展示的完整
        //var count = paint.breakText(text, true, width.toFloat(), measureWidth)
        //绘制第一行
        //canvas.drawText(text, 0, count, 0f, - fontMetrics.top, paint)
        //var oldCount = count

        //绘制第二行
        //count = paint.breakText(text, count, text.length, true, width.toFloat(), measureWidth)
        //canvas.drawText(text, oldCount, oldCount + count, 0f, - fontMetrics.top + paint.fontSpacing, paint)

        //这样写，写到第几行是个头？
        //用循环来将整段话绘制出来
        var start = 0
        var count: Int
        var verticalOffset = - fontMetrics.top //初始的文字垂直偏移值
        var maxWidth: Float

        while(start < text.length){
            //判断纵坐标，来决定宽度是否需要 -IMAGE_SIZE

            //这里是在上下的状态，展示全宽
            if (verticalOffset + fontMetrics.bottom < IMAGE_PADDING ||
                verticalOffset + fontMetrics.top > IMAGE_PADDING + IMAGE_SIZE) {
                maxWidth = width.toFloat()
            } else {
                //这里是在中间的状态，展示 -IMAGE_SIZE
                maxWidth = width.toFloat() - IMAGE_SIZE
            }

            count = paint.breakText(text, start, text.length, true, maxWidth, measuredWidth)
            canvas.drawText(text, start, start + count, 0f, verticalOffset, paint)
            //刷新值
            start += count
            verticalOffset += paint.fontSpacing
        }
    }
}
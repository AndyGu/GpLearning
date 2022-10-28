package com.bard.gplearning.widgets.hencoder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.bard.gplearning.extensions.dp
import com.bard.gplearning.widgets.hencoder.AvatarView.Companion.getAvatar


private val BITMAP_SIZE = 200.dp
private val BITMAP_PADDING = 100.dp

/**
 * 裁切
 *    - clipRect() //切矩形
 *    - clipPath() //切
 *    - clipOutRect()/clipOutPath() //切出来的不要
 *
 *  用 clipPath 也能切圆形头像，但是一般不用，而是用Xfermode，因为无毛..边
 */
class ClipView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, BITMAP_SIZE.toInt())

    private val clipped = Path().apply{
        addOval(BITMAP_PADDING, BITMAP_PADDING,
            BITMAP_PADDING + BITMAP_SIZE, BITMAP_PADDING + BITMAP_SIZE, Path.Direction.CCW)
    }

    override fun onDraw(canvas: Canvas){
        //一，先尝试切一切左上角
        //canvas.clipRect(BITMAP_PADDING, BITMAP_PADDING,
        //      BITMAP_PADDING + BITMAP_SIZE/2, BITMAP_PADDING + BITMAP_SIZE/2)

        //二，切个圆形头像出来
        //Xfermode没毛边，clipPath有毛边
        canvas.clipPath(clipped)

        canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)
    }
}
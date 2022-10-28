package com.bard.gplearning.widgets.hencoder

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.bard.gplearning.R
import com.bard.gplearning.extensions.dp


private val IMAGE_WIDTH = 200f.dp
private val IMAGE_PADDING = 20f.dp
private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

class AvatarView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val bounds = RectF(IMAGE_PADDING,
            IMAGE_PADDING,
        IMAGE_PADDING + IMAGE_WIDTH,
        IMAGE_PADDING + IMAGE_WIDTH)


    override fun onDraw(canvas: Canvas){

        val count = canvas.saveLayer(bounds, null)

        canvas.drawOval(IMAGE_PADDING, IMAGE_PADDING,
            IMAGE_PADDING + IMAGE_WIDTH,
            IMAGE_PADDING + IMAGE_WIDTH, paint)

        //只剩一种 PorterDuffXfermode   Porter/Duff 都是人名， Xfer 就是 Transfer
        //source image即将要画的图  destination image已经画好的图
        //source 只保留新的，旧的删除
        //source over 都保留，新的压旧的
        //source in 相交的保留，新的压旧的, 交叉保留，其余全删除
        //source atop 相交的保留，新的压旧的, 且保留旧的，删除新的

        //destination 只保留旧的，新的删除
        //destination over 都保留，旧的压新的
        //destination in 相交的保留，旧的压新的, 其余全删除


        //不用离屏缓冲是无效的，离屏缓冲：额外单独用一个底做绘制，绘制完再放回去，但是很耗资源
        paint.xfermode = XFERMODE

        canvas.drawBitmap(getAvatar(resources, IMAGE_WIDTH.toInt()), IMAGE_PADDING, IMAGE_PADDING, paint)

        //好习惯
        paint.xfermode = null
        canvas.restoreToCount(count)

        resources
    }

    companion object{
        fun getAvatar(resources : Resources, width: Int): Bitmap {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeResource(resources, R.drawable.sad, options)
            options.inJustDecodeBounds = false
            options.inDensity = options.outWidth
            options.inTargetDensity = width
            return BitmapFactory.decodeResource(resources, R.drawable.sad, options)
        }
    }
}
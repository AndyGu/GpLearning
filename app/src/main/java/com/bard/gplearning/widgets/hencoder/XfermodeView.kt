package com.bard.gplearning.widgets.hencoder

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.bard.gplearning.extensions.dp


// 注意注意注意
// 所谓source image 和 destination image，是包含其所在的底的
// 所以在看xfermode效果的时候，
// 要看source 和 destination的底是否大小一致且重合，
// 不然效果可能与预期不符
private val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

class XfermodeView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bounds = RectF(150f.dp, 50f.dp, 300f.dp, 250f.dp)

    private val circleBitmap = Bitmap.createBitmap(150f.dp.toInt(), 150f.dp.toInt(),
        Bitmap.Config.ARGB_8888)
    private val squareBitmap = Bitmap.createBitmap(150f.dp.toInt(), 150f.dp.toInt(),
        Bitmap.Config.ARGB_8888)

//  错误做法
//    override fun onDraw(canvas: Canvas){
//        开离屏缓冲
//        val count = canvas.saveLayer(bounds, null)
//        paint.color = Color.parseColor("#D81B60")
//        先画圆
//        canvas.drawOval(200f.px, 50f.px, 300f.px, 150f.px, paint)
//        切mode
//        paint.xfermode = XFERMODE
//        paint.color = Color.parseColor("#2196F3")
//        再画方
//        canvas.drawRect(150f.px, 100f.px, 250f.px, 200f.px, paint)
//        paint.xfermode = null
//        canvas.restoreToCount(count)
//    }



    init{
        val canvas = Canvas(circleBitmap)
        paint.color = Color.parseColor("#D81B60")
        //注意修正偏移量，减去的就是偏移量
        canvas.drawOval(200f.dp - 150f.dp,
            50f.dp - 50f.dp,
            300f.dp - 150f.dp,
            150f.dp - 50f.dp, paint)

        paint.color = Color.parseColor("#2196F3")
        canvas.setBitmap(squareBitmap)
        //注意修正偏移量，减去的就是偏移量
        canvas.drawRect(150f.dp - 150f.dp, 100f.dp - 50f.dp, 250f.dp - 150f.dp, 200f.dp - 50f.dp, paint)
    }



    override fun onDraw(canvas: Canvas){
        val count = canvas.saveLayer(bounds, null)

        //使用drawBitmap的形式修改 因为底不同而产生的xfermode效果不正确的问题
        canvas.drawBitmap(circleBitmap, 150f.dp, 50f.dp, paint)
        paint.xfermode = XFERMODE
        canvas.drawBitmap(squareBitmap, 150f.dp, 50f.dp, paint)

        paint.xfermode = null
        canvas.restoreToCount(count)
    }
}
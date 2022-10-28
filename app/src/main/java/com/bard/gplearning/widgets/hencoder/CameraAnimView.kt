package com.bard.gplearning.widgets.hencoder

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave
import com.bard.gplearning.extensions.dp
import com.bard.gplearning.widgets.hencoder.AvatarView.Companion.getAvatar


/**
 * 用二维的操作，模仿三维的操作
 */

private val BITMAP_SIZE = 200.dp
private val BITMAP_PADDING = 100.dp

class CameraAnimView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, BITMAP_SIZE.toInt())
    private val camera = Camera()

    var topFlip = 0f
        set(value){
            field = value
            invalidate()
        }

    var bottomFlip = 30f
        set(value){
            field = value
            invalidate()
        }

    var flipRotation = 0f
        set(value){
            field = value
            invalidate()
        }


    init{
        //不能放初始化了，改变bottomFlip需要在onDraw时调用
//        camera.rotateX(30f)

        //解决糊脸问题，将z轴坐标向后推  1英寸 = 72px
        camera.setLocation(0f, 0f, -8f * resources.displayMetrics.density) //1英寸 = 72px
    }

    override fun onDraw(canvas: Canvas){
        /**
         * 做个图片半边翻页效果
         */
        //上半部分
        canvas.save()
        canvas.translate(BITMAP_PADDING + BITMAP_SIZE/2, BITMAP_PADDING + BITMAP_SIZE/2)
        canvas.rotate(-flipRotation)

        camera.save()
        camera.rotateX(topFlip)
        camera.applyToCanvas(canvas)
        camera.restore()

        canvas.clipRect(-BITMAP_SIZE, -BITMAP_SIZE, BITMAP_SIZE, 0f)
        canvas.rotate(flipRotation)
        canvas.translate(-(BITMAP_PADDING + BITMAP_SIZE/2), -(BITMAP_PADDING + BITMAP_SIZE/2))
        canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)
        canvas.restore()

        //下半部分 ktx的便捷，省略save 和 restore
        canvas.withSave{
            //canvas.save()
            canvas.translate(BITMAP_PADDING + BITMAP_SIZE/2, BITMAP_PADDING + BITMAP_SIZE/2)
            canvas.rotate(-flipRotation)

            /**
             * 单单放在这会有问题，每次后台-前台会重绘，折痕会累加，所以也要save restore
             */
            camera.save()
            camera.rotateX(bottomFlip)
            camera.applyToCanvas(canvas)
            camera.restore()

            canvas.clipRect(-BITMAP_SIZE, 0f, BITMAP_SIZE, BITMAP_SIZE)
            canvas.rotate(flipRotation)
            canvas.translate( -(BITMAP_PADDING + BITMAP_SIZE/2), -(BITMAP_PADDING + BITMAP_SIZE/2))
            canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)
            //canvas.restore()
        }
    }
}
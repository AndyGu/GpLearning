package com.bard.gplearning.widgets.hencoder

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.bard.gplearning.extensions.dp
import com.bard.gplearning.widgets.hencoder.AvatarView.Companion.getAvatar


/**
 * 用二维的操作，模仿三维的操作
 *
 * Camera的坐标系，与高中的相同
 * z轴，屏幕里的、插进去的是正，屏幕外的是负
 *      ↑y 正
 *      |
 * -----|------> x 正
 *      |
 *      |
 */

private val BITMAP_SIZE = 200.dp
private val BITMAP_PADDING = 100.dp

class CameraView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, BITMAP_SIZE.toInt())
    private val camera = Camera()

    init{
        //只这样做并没有设定旋转轴心，默认是x轴旋转了30度，
        //怎么旋的？x轴箭头指向屏幕外，顺时针转30度
        //或者箭头指向屏幕内，逆时针30度
        camera.rotateX(30f)

        //解决糊脸问题，将z轴坐标向后推
        camera.setLocation(0f, 0f, -8f * resources.displayMetrics.density) //1英寸 = 72px
    }

    override fun onDraw(canvas: Canvas){
//        // 一，尝试先平移，旋转了，再移回来
//        // 移到原点
//        canvas.translate(-(BITMAP_PADDING + BITMAP_SIZE/2), -(BITMAP_PADDING + BITMAP_SIZE/2))
//        // 旋转x轴，做投影
//        camera.applyToCanvas(canvas);
//        // 再移回来
//        canvas.translate(BITMAP_PADDING + BITMAP_SIZE/2, BITMAP_PADDING + BITMAP_SIZE/2)
//        // 绘制图片
//        canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)

        /**
         * 结果是空白
         * 应该倒着执行
         * 因为你移动的是 画布！！！移动的不是图像！！！
         */
//        // 二
//        // 真正的图像移到原点，是画布/坐标往右移
//        canvas.translate(BITMAP_PADDING + BITMAP_SIZE/2, BITMAP_PADDING + BITMAP_SIZE/2)
//        // 旋转x轴，做投影
//        camera.applyToCanvas(canvas);
//        // 真正的图像移动回来，是画布/坐标往左移
//        canvas.translate(-(BITMAP_PADDING + BITMAP_SIZE/2), -(BITMAP_PADDING + BITMAP_SIZE/2))
//        // 绘制图片
//        canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)
        /**
         * 正常了
         */


        /**
         * 做个图片半边翻页效果
         *
         */
        //图片上半部分 不动
        canvas.save()
        //6.把图片从坐标中心移到原来的中心
        canvas.translate(BITMAP_PADDING + BITMAP_SIZE/2, BITMAP_PADDING + BITMAP_SIZE/2)
        //5.把图旋正，
        canvas.rotate(-30f)

        //4.裁切，保留上半部，因为图现在是歪的，整体宽变大了
        //所以，旋转后需要增大裁切范围，不然旋转后会裁到角
        //左 上 右 下
//        canvas.clipRect(- BITMAP_SIZE/2, -BITMAP_SIZE/2, BITMAP_SIZE/2, 0f)
        canvas.clipRect(-BITMAP_SIZE, -BITMAP_SIZE, BITMAP_SIZE, 0f)

        //3.图顺时针转30度，做斜着切的效果，趁着图在原点时候旋转
        canvas.rotate(30f)
        //2.把图挪到坐标原点
        canvas.translate(-(BITMAP_PADDING + BITMAP_SIZE/2), -(BITMAP_PADDING + BITMAP_SIZE/2))
        //1.画图
        canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)
        canvas.restore()


        //下半部分
        canvas.save()
        //切记，是移动轴，不是移动图，所以倒着想就是以图的角度想更方便
        //7.移回到绘图位置
        canvas.translate(BITMAP_PADDING + BITMAP_SIZE/2, BITMAP_PADDING + BITMAP_SIZE/2)
        //6.图片旋转正回来
        canvas.rotate(-30f)

        //5.应用翻页效果
        camera.applyToCanvas(canvas)

        //4.裁切，保留下半部，因为图现在是歪的，整体宽变大了
        //canvas.clipRect(- BITMAP_SIZE/2, 0, BITMAP_SIZE/2, BITMAP_SIZE/2)
        canvas.clipRect(- BITMAP_SIZE, 0f, BITMAP_SIZE, BITMAP_SIZE)

        //3.旋转图片，做斜着切的效果，趁着图在原点时候旋转【倒着看】
        canvas.rotate(30f)
        //2.把图移到坐标中心
        canvas.translate( -(BITMAP_PADDING + BITMAP_SIZE/2), -(BITMAP_PADDING + BITMAP_SIZE/2))
        //1.画图在指定位置
        canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)

        canvas.restore()
    }
}
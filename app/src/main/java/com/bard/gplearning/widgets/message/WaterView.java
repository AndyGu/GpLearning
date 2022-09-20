package com.bard.gplearning.widgets.message;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bard.gplearning.R;


/**
 * 实现步骤
 * 1.自定义控件，监听onTouchEvent的down，up以及move方法
 * 2.滑出起点圆以及终点圆
 * 3.得到两个圆之间的连接桥坐标 进行路径绘制
 * 4.各种逻辑之间的判断以及处理
 */
public class WaterView extends FrameLayout {
    //定义一个文本文件
    private TextView textView;
    //文本框的初始坐标
    private PointF initPosition;
    //手指是否触摸到了控件
    private boolean isClick = false;
    //手指一动到的坐标，或者 也可以叫做终点坐标
    private PointF movePosition;
    //绘制的圆的半径
    private float mRadius = 40;
    //绘制的画笔
    private Paint paint;
    //存储连接桥路径的对象
    private Path path;
    //判断文本框是否离开了某个范围之外
    private boolean isOut = false;
    //爆炸效果的图片空间
    private ImageView imageView;


    public WaterView(@NonNull Context context) {
        super(context);
        init();
    }


    /**
     * 初始化整个控件
     */
    private void init() {
        initPosition = new PointF(500, 500);
        movePosition = new PointF();

        //初始化画笔
        paint = new Paint();
        //初始化画笔的颜色
        paint.setColor(Color.RED);
        //初始化画笔的样式
        paint.setStyle(Paint.Style.FILL);

        path = new Path();

        textView = new TextView(getContext());
        textView.setPadding(20, 20, 20, 20);
        textView.setText("99+");
        textView.setTextSize(10);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.textview_bg);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        this.addView(textView);

        imageView = new ImageView(getContext());
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.drawable.anim);
        imageView.setVisibility(GONE);
        this.addView(imageView);
    }




    /**
     * 绘制当前控件里面的内容/里面的控件 的方法
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        //保存canvas的状态
        canvas.save();

        if(isClick){
            textView.setX(movePosition.x - textView.getWidth()/2);
            textView.setY(movePosition.y - textView.getHeight()/2);

            drawPath();

            if(!isOut){
                //画第一个圆，也就是初始坐标的圆
                canvas.drawCircle(initPosition.x, initPosition.y, mRadius, paint);
                //画第二个圆，也就是终点的圆
                canvas.drawCircle(movePosition.x, movePosition.y, mRadius, paint);
                //画连接桥
                canvas.drawPath(path, paint);
            }

        }else{
            textView.setX(initPosition.x - textView.getWidth()/2);
            textView.setY(initPosition.y - textView.getHeight()/2);
        }

        //恢复canvas的状态
        canvas.restore();

        super.dispatchDraw(canvas);
    }

    /**
     * 找到绘制连接桥的四个点 并且用 贝塞尔曲线
     */
    public void drawPath(){
        //获取到终点与起点的X坐标的差值
        float widthX = movePosition.x - initPosition.x;
        //获取到终点与起点的Y坐标的差值
        float heightY = movePosition.y - initPosition.y;

        //获取两个点之间的直线距离
        float distance = (float) Math.sqrt(Math.pow(widthX, 2) + Math.pow(heightY, 2));
        mRadius = 40 - distance/30;
        if(distance >= 400){
            isOut = true;
        }else{
            isOut = false;
        }

        //得到三角形的锐角正切值,然后反正切，得到角度
        double degree = Math.atan(heightY / widthX);

        //获取到offsetX边的长度
        float offsetX  = (float)(mRadius * Math.sin(degree));
        //获取到offsetY边的长度
        float offsetY  = (float)(mRadius * Math.cos(degree));

        //获取到iTop坐标
        float iTopX = initPosition.x - offsetX;
        float iTopY = initPosition.y + offsetY;

        //获取到mTop坐标
        float mTopX = movePosition.x - offsetX;
        float mTopY = movePosition.y + offsetY;

        //获取到iBottom坐标
        float iBottomX = initPosition.x + offsetX;
        float iBottomY = initPosition.y - offsetY;

        //获取到mBottom坐标
        float mBottomX = movePosition.x + offsetX;
        float mBottomY = movePosition.y - offsetY;

        //获取到起点坐标和终点坐标 的中心点坐标
        float centerX = (initPosition.x + movePosition.x)/2;
        float centerY = (initPosition.y + movePosition.y)/2;

        //初始化path对象
        path.reset();
        //将起点移动到A坐标
        path.moveTo(iTopX, iTopY);
        //从iTop坐标连接到mTop坐标
        path.quadTo(centerX, centerY, mTopX, mTopY);
        //从mTop坐标连接到mBottom坐标
        path.lineTo(mBottomX, mBottomY);
        //从mBottom坐标连接到iBottom坐标
        path.quadTo(centerX, centerY, iBottomX, iBottomY);
        //回到iTop点
        path.lineTo(iTopX, iTopY);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                movePosition.set(initPosition.x, initPosition.y);
                //判断当前点击的位置 是否是在文本控件里面
                Rect rect = new Rect();//这个对象是用来封装文本控件的范围的对象
                int[] location = new int[2];
                //获取到textView控件在窗体中的x,y坐标
                textView.getLocationOnScreen(location);
                //初始化Rect对象
                rect.left = location[0];
                rect.top = location[1];
                rect.right = location[0] + textView.getWidth();
                rect.bottom = location[1] + textView.getHeight();

                //判断当前点击的坐标 是否在范围之内
                if(rect.contains((int)event.getRawX(), (int)event.getRawY())){ //getX -- 相对于屏幕的坐标
                    isClick = true;
                }

                break;
            case MotionEvent.ACTION_UP:
                isClick = false;
                if(isOut){
                    textView.setVisibility(View.GONE);
                    imageView.setX(movePosition.x - imageView.getWidth()/2);
                    imageView.setY(movePosition.y - imageView.getHeight()/2);
                    imageView.setVisibility(View.VISIBLE);
                    ((AnimationDrawable)(imageView.getDrawable())).start();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                movePosition.set(event.getX(), event.getY());//getRawX -- 相对于父控件的坐标
                break;
        }
        //通过这个api可以调用到dispatchDraw的方法
        postInvalidate();
        return true;
    }

    /**
     * 绘制包括本身的
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}

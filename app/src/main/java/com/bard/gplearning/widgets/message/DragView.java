package com.bard.gplearning.widgets.message;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bard.gplearning.R;


/**
 * 实现步骤
 * 1.自定义控件，监听onTouchEvent的down，up以及move方法
 * 2.滑出起点圆以及终点圆
 * 3.得到两个圆之间的连接桥坐标 进行路径绘制
 * 4.各种逻辑之间的判断以及处理
 */
public class DragView extends View {

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }


    // 手指按下的坐标
    int startX = 0, startY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //记录按下时手指的坐标
                startX = (int) event.getRawX();
                startY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                //获取移动后的坐标
                int movedX = (int) event.getRawX();
                int movedY = (int) event.getRawY();
                //拿到手指移动距离的大小
                int moveOffsetX = movedX - startX;
                int moveOffsetY = movedY - startY;
                //拿到当前控件移动之前的坐标（未作layout操作，控件位置暂未变）
                int left = getLeft();
                int top = getTop();
                left += moveOffsetX;
                top += moveOffsetY;
                int right = left + getWidth();
                int bottom = top + getHeight();
                //重新布局
                layout(left, top, right, bottom);
                //重置了起始坐标
                startX = movedX;
                startY = movedY;
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }


}

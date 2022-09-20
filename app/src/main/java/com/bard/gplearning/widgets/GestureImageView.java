package com.bard.gplearning.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;


public class GestureImageView extends AppCompatImageView {
    private static final float INIT_SCALE_FACTOR = 1.2f; // 初始化缩放比例
    private float mScaleFactor; // 缩放比例
    private Matrix mMatrix = new Matrix(); // 用于图片旋转、平移、缩放的矩阵
    private RectF mImageRect = new RectF(); // 图片区域矩形
    private PointF scaleCenter = new PointF();
    private PaintFlagsDrawFilter mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    private PointF mLastPoint1 = new PointF(); // 上次事件的第一个触点
    private PointF mLastPoint2 = new PointF(); // 上次事件的第二个触点
    private PointF mCurrentPoint1 = new PointF(); // 本次事件的第一个触点
    private PointF mCurrentPoint2 = new PointF(); // 本次事件的第二个触点
    private boolean mCanScale = false; // 是否可以缩放

    protected PointF mLastCenterPoint = new PointF(); // 图片平移时记录上一次ACTION_MOVE的中心点
    private PointF mCurrentCenterPoint = new PointF(); // 当前各触点的中心点
    protected boolean mCanDrag = false; // 是否可以平移

    private PointF mLastVector = new PointF(); // 记录上一次触摸事件两指所表示的向量
    private PointF mCurrentVector = new PointF(); // 记录当前触摸事件两指所表示的向量
    private boolean mCanRotate = false; // 判断是否可以旋转

    protected boolean isTransforming = false; // 图片是否正在变化

    public GestureImageView(Context context) {
        this(context, null);
    }

    public GestureImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutImg();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(mDrawFilter);
        super.onDraw(canvas);
    }


    /**
     * 设置图片布局位置和大小
     */
    private void layoutImg() {
        mMatrix.reset();
        refreshImageRect();

        // 根据图片区域，计算缩放比例
        mScaleFactor = Math.min(getWidth() / mImageRect.width(), getHeight() / mImageRect.height());
        mScaleFactor *= INIT_SCALE_FACTOR;
        mMatrix.postScale(mScaleFactor, mScaleFactor, mImageRect.centerX(), mImageRect.centerY());

        refreshImageRect();
        // 移动图片到中心
        mMatrix.postTranslate((getRight() - getLeft()) / 2f - mImageRect.centerX(), (getBottom() - getTop()) / 2f - mImageRect.centerY());
        setMatrix();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF midPoint = getTouchEventCenterPoint(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                // 每次触摸事件开始都初始化mLastCenterPoint
                mLastCenterPoint.set(midPoint);
                isTransforming = false;

                if (event.getPointerCount() == 2) {
                    mCanDrag = false;
                    mCanScale = true;
                    mCanRotate = true;

                    mLastPoint1.set(event.getX(0), event.getY(0));
                    mLastPoint2.set(event.getX(1), event.getY(1));
                    mLastVector.set(event.getX(1) - event.getX(0), event.getY(1) - event.getY(0));
                } else if(event.getPointerCount() == 1) {
                    mCanDrag = true;
                    mCanRotate = false;
                    mCanScale = false;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mCanDrag){
                    translate(midPoint);
                }

                if (mCanScale){
                    scale(event);
                }

                if (mCanRotate){
                    rotate(event);
                }
                // 判断图片是否发生了变换
                if (!getImageMatrix().equals(mMatrix)){
                    isTransforming = true;
                }

                if (mCanDrag || mCanScale || mCanRotate){
                    setMatrix();
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:
                mCanScale = false;
                mCanDrag = false;
                mCanRotate = false;
                break;
        }
        return true;
    }

    private void rotate(MotionEvent event) {
        // 计算当前两指触点所表示的向量
        mCurrentVector.set(event.getX(1) - event.getX(0), event.getY(1) - event.getY(0));
        float degree = getRotateDegree(mLastVector, mCurrentVector);
        mMatrix.postRotate(degree, mImageRect.centerX(), mImageRect.centerY());
        mLastVector.set(mCurrentVector);
    }


    private float getRotateDegree(PointF lastVector, PointF currentVector) {
        //上次触摸事件向量与x轴夹角
        double lastRadius = Math.atan2(lastVector.y, lastVector.x);
        //当前触摸事件向量与x轴夹角
        double currentRadius = Math.atan2(currentVector.y, currentVector.x);
        // 两向量与x轴夹角之差即为需要旋转的角度
        double radius = currentRadius - lastRadius;
        return (float) Math.toDegrees(radius);
    }

    protected void translate(PointF midPoint) {
        float dx = midPoint.x - mLastCenterPoint.x;
        float dy = midPoint.y - mLastCenterPoint.y;
        mMatrix.postTranslate(dx, dy);
        mLastCenterPoint.set(midPoint);
    }

    /**
     * 获取触点的中点
     */
    private PointF getTouchEventCenterPoint(MotionEvent event) {
        mCurrentCenterPoint.set(0f, 0f);
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            mCurrentCenterPoint.x += event.getX(i);
            mCurrentCenterPoint.y += event.getY(i);
        }
        mCurrentCenterPoint.x /= pointerCount;
        mCurrentCenterPoint.y /= pointerCount;
        return mCurrentCenterPoint;
    }


    private void scale(MotionEvent event) {
        scaleCenter.set(mImageRect.centerX(), mImageRect.centerY());
        // 初始化当前两指触点
        mCurrentPoint1.set(event.getX(0), event.getY(0));
        mCurrentPoint2.set(event.getX(1), event.getY(1));
        // 计算缩放比例
        float scaleFactor = distance(mCurrentPoint1, mCurrentPoint2) / distance(mLastPoint1, mLastPoint2);

        mMatrix.postScale(scaleFactor, scaleFactor, scaleCenter.x, scaleCenter.y);
        mLastPoint1.set(mCurrentPoint1);
        mLastPoint2.set(mCurrentPoint2);
    }

    /**
     * 获取两点间距离
     */
    private float distance(PointF point1, PointF point2) {
        float dx = point2.x - point1.x;
        float dy = point2.y - point1.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 设置图片所在区域，并将矩阵应用到图片
     */
    protected void setMatrix() {
        refreshImageRect(); /*将矩阵映射到ImageRect*/
        setImageMatrix(mMatrix);
    }

    /**
     * 图片使用矩阵变换后，刷新图片所对应的mImageRect所指示的区域
     */
    private void refreshImageRect() {
        if (getDrawable() != null) {
            mImageRect.set(getDrawable().getBounds());
            mMatrix.mapRect(mImageRect, mImageRect);
        }
    }
}
package com.bard.gplearning.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.bard.gplearning.R;

public class PwdEditText extends AppCompatEditText implements TextWatcher {
    private int mDefaultBgColor = Color.WHITE;
    private int mDefaultBgWidth = 5;
    private int mDefaultBgRadius = 10;
    private int mDefaultDivisionColor = Color.WHITE;
    private int mDefaultDivisionWidth = 5;
    private int mDefaultPwdCount = 6;
    private int mDefaultPwdColor = Color.BLACK;
    private int mDefaultPwdSize = 14;

    //画笔
    private Paint mPaint;
    //背景框颜色
    private int mBgColor;
    //背景框线的宽度
    private int mBgWidth;
    //背景框的圆角
    private int mBgRadius;

    //分割线的颜色
    private int mDivisionColor;
    //分割线的宽度
    private int mDivisionWidth;

    //总密码的个数
    private int mPwdCount;
    //密码圆点的颜色
    private int mPwdColor;
    //密码圆点的大小
    private int mPwdSize;

    private PwdCompleteListener pwdCompleteListener;

    public PwdEditText(Context context) {
        this(context, null);
    }

    public PwdEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PwdEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PwdEditText);
            mBgColor = typedArray.getColor(R.styleable.PwdEditText_bg_color, mDefaultBgColor);
            mBgWidth = (int) typedArray.getDimension(R.styleable.PwdEditText_bg_width, mDefaultBgWidth);
            mBgRadius = (int) typedArray.getDimension(R.styleable.PwdEditText_bg_radius, mDefaultBgRadius);
            mPwdCount = typedArray.getInteger(R.styleable.PwdEditText_password_count, mDefaultPwdCount);
            mDivisionColor = typedArray.getColor(R.styleable.PwdEditText_division_color, mDefaultDivisionColor);
            mDivisionWidth = (int) typedArray.getDimension(R.styleable.PwdEditText_division_width, mDefaultDivisionWidth);
            mPwdColor = typedArray.getColor(R.styleable.PwdEditText_password_color, mDefaultPwdColor);
            mPwdSize = (int) typedArray.getDimension(R.styleable.PwdEditText_password_size, mDefaultPwdSize);
            typedArray.recycle();
        }
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setDither(true); //防抖动
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mPwdCount)}); //最大输入长度
        setInputType(InputType.TYPE_CLASS_NUMBER); //输入类型
        setFocusable(true); //点击弹出输入框
        setFocusableInTouchMode(true);
        addTextChangedListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBg(canvas);
        drawDivision(canvas);
        drawPassword(canvas);
    }

    /**
     * 绘制背景
     */
    private void drawBg(Canvas canvas) {
        RectF rectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBgWidth);
        canvas.drawRoundRect(rectF, mBgRadius, mBgRadius, mPaint);
    }

    /**
     * 绘制分割线
     */
    private void drawDivision(Canvas canvas) {
        int itemWith = (getMeasuredWidth() - (2 * mBgWidth)) / mPwdCount;
        mPaint.setColor(mDivisionColor);
        mPaint.setStrokeWidth(mDivisionWidth);
        for (int i = 0; i < mPwdCount - 1; i++) {
            float startX = mBgWidth + (itemWith * (i + 1));
            float startY = 0;
            float endY = startY + getMeasuredHeight();
            canvas.drawLine(startX, startY, startX, endY, mPaint);
        }
    }

    /**
     * 绘制圆点密码
     */
    private void drawPassword(Canvas canvas) {
        //每个密码输入框的宽度
        int itemWith = (getMeasuredWidth() - (2 * mBgWidth)) / mPwdCount;
        mPaint.setColor(mPwdColor);
        mPaint.setStrokeWidth(mPwdSize);
        mPaint.setStyle(Paint.Style.FILL);
        String text = null;
        if(getText() != null) {
            text = getText().toString().trim();
        }
        if (text != null && !TextUtils.isEmpty(text)) {
            for (int i = 0; i < text.length(); i++) {
                float cx = mBgWidth + (itemWith / 2f) + itemWith * i;
                float cy = getMeasuredHeight() / 2f;
                canvas.drawCircle(cx, cy, mPwdSize, mPaint);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (pwdCompleteListener != null &&
                !TextUtils.isEmpty(s) && s.length() == mPwdCount) {
            pwdCompleteListener.inputComplete(s.toString().trim());
        }
    }

    public void setPwdCompleteListener(PwdCompleteListener pwdCompleteListener) {
        this.pwdCompleteListener = pwdCompleteListener;
    }

    public interface PwdCompleteListener {
        void inputComplete(String password);
    }
}

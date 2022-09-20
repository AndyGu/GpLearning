package com.bard.gplearning.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.bard.gplearning.utils.PixUtils;

public class ScreenAdaptLayout extends RelativeLayout {
    private boolean flag;

    public ScreenAdaptLayout(Context context) {
        super(context);
    }

    public ScreenAdaptLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScreenAdaptLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!flag){
            float scaleX = PixUtils.getInstance(getContext()).getHorizontalScale();
            float scaleY = PixUtils.getInstance(getContext()).getVerticalScale();

            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                LayoutParams params = (LayoutParams) child.getLayoutParams();

                params.width = (int) (params.width * scaleX);
                params.height = (int) (params.height * scaleY);
                params.leftMargin = (int) (params.leftMargin * scaleX);
                params.rightMargin = (int) (params.rightMargin * scaleX);
                params.topMargin = (int) (params.topMargin * scaleY);
                params.bottomMargin = (int) (params.bottomMargin * scaleY);

            }

            flag = true;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

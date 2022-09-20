package com.bard.gplearning.widgets.explosion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.bard.gplearning.utils.ExplosionUtils;
import java.util.ArrayList;

public class ExplosionField extends View {
    private ArrayList<ExplosionAnimator> explosionAnimators;
    private ParticleFactory mPartcleFactory;
    private OnClickListener onClickListener;


    public ExplosionField(Context context, ParticleFactory particleFactory) {
        super(context);
        explosionAnimators = new ArrayList<>();
        mPartcleFactory = particleFactory;
        //将动画区域添加到整个界面之上

        attachToActivity();

    }

    private void attachToActivity() {
        ViewGroup rootView = ((Activity)getContext()).findViewById(Window.ID_ANDROID_CONTENT);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.addView(this, layoutParams);
    }


    /**
     *
     * @param view 需要有粒子特效的view
     */
    public void addListener(View view){
        if(view instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) view;
            int viewCount = viewGroup.getChildCount();

            for(int i=0; i<viewCount; i++){
                addListener(viewGroup.getChildAt(i));
            }
        }else{
            view.setClickable(true);
            view.setOnClickListener(getOnClickListener());
        }

    }

    private OnClickListener getOnClickListener() {
        if(onClickListener == null){
            onClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExplosionField.this.explore(v);
                }
            };
        }
        return onClickListener;
    }


    //爆炸第一阶段
    private void explore(final View view) {
        //先获取view的区域位置
        final Rect rect = new Rect();
        view.getGlobalVisibleRect(rect); //获取view相对于整个屏幕的位置（包含statusbar, actionbar）

        //标题栏高度
        int contentTop = ((ViewGroup)getParent()).getTop();

        //状态栏高度
        Rect frame = new Rect();
        ((Activity)getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        rect.offset(0, -contentTop - statusBarHeight);
        if(rect.width() == 0|| rect.height() == 0){
            return;
        }

        //震动动画
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationX((ExplosionUtils.RANDOM.nextFloat() - 0.5f) * view.getWidth() * 0.05f);
                view.setTranslationY((ExplosionUtils.RANDOM.nextFloat() - 0.5f) * view.getHeight() * 0.05f);
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //开始爆炸特效
                explore(view, rect);
            }
        });

        animator.start();
    }

    //爆炸第二阶段
    private void explore(final View view, Rect bound){
        final ExplosionAnimator animator = new ExplosionAnimator(this, ExplosionUtils.createBitmapFromView(view), bound, mPartcleFactory);
        explosionAnimators.add(animator);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setClickable(false);
                //缩小透明
                view.animate().setDuration(150).scaleX(0f).scaleY(0f).alpha(0f).start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //恢复大小透明度
                view.animate().setDuration(150).scaleX(1f).scaleY(1f).alpha(1f).start();
                view.setClickable(true);

                explosionAnimators.remove(animator);
            }
        });
        animator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(ExplosionAnimator explosionAnimator : explosionAnimators){
            explosionAnimator.draw(canvas);
        }
    }
}

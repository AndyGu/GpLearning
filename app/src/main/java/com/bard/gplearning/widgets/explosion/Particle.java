package com.bard.gplearning.widgets.explosion;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Particle {
    //属性
    float cx;
    float cy;
    int color;

    public Particle(float cx, float cy, int color) {
        this.cx = cx;
        this.cy = cy;
        this.color = color;
    }


    //计算
    protected abstract void calculate(float factor);

    //绘制
    protected abstract void draw(Canvas canvas, Paint paint);

    //逐步绘制
    public void advance(Canvas canvas, Paint paint, float factor){
        calculate(factor);
        draw(canvas, paint);
    }
}

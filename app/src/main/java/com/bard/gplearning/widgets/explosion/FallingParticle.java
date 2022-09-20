package com.bard.gplearning.widgets.explosion;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bard.gplearning.utils.ExplosionUtils;

public class FallingParticle extends Particle {

    float radius = FallingParticleFactory.PART_WH;
    float alpha = 1.0f;
    Rect mBound;


    public FallingParticle(float cx, float cy, int color, Rect bound) {
        super(cx, cy, color);
        mBound = bound;
    }

    @Override
    protected void calculate(float factor) {
        cx = cx + factor * ExplosionUtils.RANDOM.nextInt(mBound.width()) * (ExplosionUtils.RANDOM.nextFloat() - 0.5f);
        cy = cy + factor * ExplosionUtils.RANDOM.nextInt(mBound.height() / 2);

        radius = radius - factor * ExplosionUtils.RANDOM.nextInt(2);

        alpha = (1f - factor) + (1 + ExplosionUtils.RANDOM.nextFloat());
    }


    @Override
    protected void draw(Canvas canvas, Paint paint) {
        paint.setColor(color);
        paint.setAlpha((int) (Color.alpha(color) * alpha));

        canvas.drawCircle(cx, cy, radius, paint);
    }
}

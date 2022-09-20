package com.bard.gplearning.widgets.explosion;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class FallingParticleFactory extends ParticleFactory {
    public static final int PART_WH = 8;//默认粒子小球区域宽高

    @Override
    public Particle[][] generateParticles(Bitmap bitmap, Rect bound) {

        int w = bound.width();
        int h = bound.height();

        int partW_count = w / PART_WH;//横向个数
        int partH_count = h / PART_WH;//纵向个数

        partW_count = partW_count > 0 ? partW_count : 1; //列数
        partH_count = partH_count > 0 ? partH_count : 1; //行数

        int bitmap_part_w = bitmap.getWidth() / partW_count;
        int bitmap_part_h = bitmap.getHeight() / partH_count;

        Particle[][] particles = new Particle[partH_count][partW_count];
        for(int row =0; row < partH_count; row++){
            for(int column = 0; column < partW_count; column++){
                //取得当前粒子所在位置的颜色
                int color = bitmap.getPixel(column * bitmap_part_w, row * bitmap_part_h);
                float x = bound.left + PART_WH * column;
                float y = bound.top + PART_WH * row;

                particles[row][column] = new FallingParticle(x, y, color, bound);
            }
        }

        return particles;
    }
}

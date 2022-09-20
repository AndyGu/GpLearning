package com.bard.gplearning.widgets.explosion;

import android.graphics.Bitmap;
import android.graphics.Rect;

//粒子工厂
public abstract class ParticleFactory {
    public abstract Particle[][] generateParticles(Bitmap bitmap, Rect bound);

}

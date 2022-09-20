package com.bard.gplearning;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bard.annotation.BindPath;
import com.bard.eventbuslibrary.EventBus;
import com.bard.eventbuslibrary.GPBean;
import com.bard.gplearning.widgets.explosion.ExplosionField;
import com.bard.gplearning.widgets.explosion.FallingParticleFactory;
import com.bard.gplearning.widgets.message.WaterView;


public class WaterViewActivity extends AppCompatActivity {

    WaterView waterView;
    FrameLayout frameLayout;
    FrameLayout.LayoutParams layoutParams;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waterview);

        frameLayout = findViewById(R.id.fl_container);
        layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        waterView = new WaterView(this);
        waterView.setLayoutParams(layoutParams);
        frameLayout.removeAllViews();
        frameLayout.addView(waterView);
    }
}

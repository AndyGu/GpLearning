package com.bard.gplearning;

import android.os.Binder;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bard.annotation.BindPath;
import com.bard.eventbuslibrary.EventBus;
import com.bard.eventbuslibrary.GPBean;
import com.bard.gplearning.widgets.explosion.ExplosionField;
import com.bard.gplearning.widgets.explosion.FallingParticleFactory;


public class FifthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        findViewById(R.id.iv_flower).setVisibility(View.INVISIBLE);
    }

}

package com.bard.gplearning;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bard.arouter_annotation.BindPath;
import com.bard.eventbuslibrary.EventBus;
import com.bard.eventbuslibrary.GPBean;
import com.bard.gplearning.widgets.explosion.ExplosionField;
import com.bard.gplearning.widgets.explosion.FallingParticleFactory;


@BindPath(path = "/app/SecondActivity")
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getInstance().post(new GPBean("SecondActivity", "two"));
            }
        });


        ExplosionField explosionField = new ExplosionField(this, new FallingParticleFactory());
        explosionField.addListener(findViewById(R.id.ll_container));
    }
}

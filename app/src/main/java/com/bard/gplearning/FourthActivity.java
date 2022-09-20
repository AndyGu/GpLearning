package com.bard.gplearning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bard.annotation.BindPath;
import com.bard.eventbuslibrary.EventBus;
import com.bard.eventbuslibrary.GPBean;
import com.bard.gplearning.widgets.explosion.ExplosionField;
import com.bard.gplearning.widgets.explosion.FallingParticleFactory;


public class FourthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FourthActivity.this, FifthActivity.class);
                startActivity(intent);
            }
        });


//        ExplosionField explosionField = new ExplosionField(this, new FallingParticleFactory());
//        explosionField.addListener(findViewById(R.id.ll_container));
    }
}

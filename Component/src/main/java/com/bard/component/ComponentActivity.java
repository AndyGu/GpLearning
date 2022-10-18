package com.bard.component;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bard.arouter_annotation.BindPath;
import com.bard.arouter_api.RouterManager;

@BindPath(path = "/Component/ComponentActivity")
public class ComponentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component);


        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.getInstance().build("/app/SecondActivity").navigation(ComponentActivity.this);
            }
        });

    }
}

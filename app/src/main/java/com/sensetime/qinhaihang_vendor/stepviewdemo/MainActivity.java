package com.sensetime.qinhaihang_vendor.stepviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StepView stepView = findViewById(R.id.stepview);
        stepView.setStep(5);
    }
}

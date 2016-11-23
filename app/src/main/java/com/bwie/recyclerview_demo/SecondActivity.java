package com.bwie.recyclerview_demo;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SecondActivity extends AppCompatActivity {

    private MyAnimView animt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        animt = (MyAnimView) findViewById(R.id.anim);
        Point point1 = new Point(0, 0);
        Point point2 = new Point(0, 300);
        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), point1, point2);
        anim.setDuration(2000);
        anim.start();
        animt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.glamfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ServicesActivity extends AppCompatActivity {

    FrameLayout fv_haircut, fv_facial, fv_makeup;
    FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        fv_haircut = findViewById(R.id.fv_haircut);
        fv_facial = findViewById(R.id.fv_facial);
        fv_makeup = findViewById(R.id.fv_makeup);
        actionButton=findViewById(R.id.actionButton);

        fv_haircut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, Service_List.class);
                intent.putExtra("service_id", 2);
                startActivity(intent);
            }
        });

        fv_facial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, Service_List.class);
                intent.putExtra("service_id", 3);
                startActivity(intent);
            }
        });

        fv_makeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, Service_List.class);
                intent.putExtra("service_id", 1);
                startActivity(intent);
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });



        animateServiceBox(R.id.fv_makeup);
        animateServiceBox(R.id.fv_haircut);
        animateServiceBox(R.id.fv_facial);
    }

    private void animateServiceBox(int resourceId) {
        View viewToAnimate = findViewById(resourceId);
        if (viewToAnimate!= null) {
            Animation slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
            viewToAnimate.startAnimation(slideInLeft);
        }
    }
}
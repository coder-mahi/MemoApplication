package com.example.internship.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

public class SplashActivity extends AppCompatActivity {
    ProgressBar pb;
    Handler handler = new Handler();
    int pstatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView gifimg = findViewById(R.id.splash_gif);
        pb = findViewById(R.id.progressBar);

        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(gifimg);
        Glide.with(this).load(R.raw.notes).into(imageViewTarget);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pstatus < 100) {
                    pstatus++;
                    pb.setProgress(pstatus);
                    handler.postDelayed(this, 30);
                }else {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 30);
    }
}
package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Loading extends AppCompatActivity {

    private ImageView sublogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 상태 바 지우기(전체화면) */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        sublogo = (ImageView)findViewById(R.id.sublogo);

        Animation anim = AnimationUtils
                .loadAnimation
                        (getApplicationContext(), R.anim.fade_in);
        sublogo.startAnimation(anim);
        startLoading();
    }

    private void startLoading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }

    @Override
    public void finish() {
        super.finish();

        this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

}

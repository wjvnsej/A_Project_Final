package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class LoginActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /* 상태 바 지우기(전체화면) */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);




        /* 로딩화면 부르기 */
        Intent intent = new Intent(this, Loading.class);
        startActivity(intent);

        videoView = (VideoView) findViewById(R.id.loginvideo);

        MediaController mediaController = new MediaController(this);

        mediaController.setAnchorView(videoView);

        final Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login);

        videoView.setMediaController(null);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

    }
}

package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    int [] resIds = { R.drawable.club, R.drawable.matching, R.drawable.manager, R.drawable.mypage };
    String[] titles = { "Club", "Matching", "Manager", "Mypage"  };

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 상태 바 지우기(전체화면) */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        /* 로딩화면 부르기 */
        Intent intent = new Intent(this, Loading.class);
        startActivity(intent);

        MenuAdapter adapter = new MenuAdapter(this, resIds, titles );
        gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(adapter);



    }
}



























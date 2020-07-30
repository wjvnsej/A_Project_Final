package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int [] resIds = {
            R.drawable.club, R.drawable.matching, R.drawable.manager,
            R.drawable.mypage, R.drawable.qna, R.drawable.ball
    };
    String[] titles = {
            "Club", "Matching", "Manager",
            "Mypage", "Q&A", "Charge"
    };

    private GridView menuView;
    private TextView logout, log_name;
    String m_id, m_name;

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

        /* 메뉴 띄우기 */
        MenuAdapter adapter = new MenuAdapter(this, resIds, titles );
        menuView = (GridView)findViewById(R.id.gridview);
        menuView.setAdapter(adapter);

        Intent mainIntent = getIntent();
        log_name = (TextView)findViewById(R.id.log_name);
        m_id = mainIntent.getStringExtra("m_id");
        m_name = mainIntent.getStringExtra("m_name");

        log_name.setText(m_name);

        SharedPreference.setAttribute(getApplicationContext(), "m_id", m_id);

        String id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();

        /* 로그아웃 */
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder alertBasic = new AlertDialog.Builder(v.getContext());
                alertBasic.setCancelable(false);
                alertBasic.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("로그아웃")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this,
                                                "로그아웃 되었습니다.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(
                                                getApplicationContext(), // 현재 화면의 제어권자
                                                LoginActivity.class); // 다음 넘어갈 클래스 지정
                                        SharedPreference.removeAllAttribute(getApplicationContext());
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /*Toast.makeText(MainActivity.this,
                                                "취소클릭합니다.",
                                                Toast.LENGTH_SHORT).show();*/
                                        dialog.cancel();
                                    }
                                })
                        .show();
            }
        });
    }

}


























